package com.github.spiceh2020.json2rdf.transformers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.apache.jena.graph.Graph;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.spiceh2020.sparql.anything.model.Triplifier;

public class JSONTransformer implements Triplifier {
	
	private static final Logger logger= LogManager.getLogger(JSONTransformer.class);

	private String propertyPrefix, uriRoot;
	private boolean useBlankNodes = true;
	public final static String PROPERTY_PREFIX_PARAMETER = "propertyPrefix";
	public final static String URI_ROOT_PARAMETER = "uriRoot";
	public final static String USE_BLANK_NODES_PARAMETER = "useBlankNodes";

	public JSONTransformer() {
		super();
	}

	public Model transformJSONFile(File input) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(input));
		StringBuilder sb = new StringBuilder();
		br.lines().forEachOrdered(l -> sb.append(l));
		br.close();
		return transformJSON(br.toString());
	}

	public Model transformJSONFromURL(URL url) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

		if (propertyPrefix == null) {
			propertyPrefix = url.toString() + "/";
		}
		StringBuilder sb = new StringBuilder();
		br.lines().forEachOrdered(l -> sb.append(l));
		br.close();

		return transformJSON(sb.toString());
	}

	public Model transformJSON(String json) {
		checkParameters();
		try {
			return getModel(new JSONObject(json));
		} catch (JSONException e) {
			return getModel(new JSONArray(json));
		}
	}

	private void checkParameters() {
		if (propertyPrefix == null)
			throw new RuntimeException("The property prefix can't be null");
	}

	public Model getModel(JSONObject object) {
		return getModel(object, createResource(uriRoot));
	}

	public Model getModel(JSONArray arr) {
		return getModel(arr, createResource(uriRoot));
	}

	private Model getModel(JSONObject object, Resource r) {
		Model m = ModelFactory.createDefaultModel();
		m.add(r, RDF.type, RDFS.Resource);
		object.keys().forEachRemaining(k -> {
			Object o = object.get(k);
			if (o instanceof String || o instanceof Boolean || o instanceof Integer) {
				transformPrimites(m, r, m.createProperty(propertyPrefix + k), o);
			} else if (o instanceof JSONObject) {
				transformJSONObject(m, r, m.createProperty(propertyPrefix + k), (JSONObject) o);
			} else if (o instanceof JSONArray) {
				transformArray(m, r, m.createProperty(propertyPrefix + k), (JSONArray) o);
			}
		});
		return m;
	}

	private Model getModel(JSONArray arr, Resource r) {
		Model m = ModelFactory.createDefaultModel();
		m.add(r, RDF.type, RDF.Seq);
		for (int i = 0; i < arr.length(); i++) {
			Object o = arr.get(i);
			if (o instanceof String || o instanceof Boolean || o instanceof Integer) {
				transformPrimites(m, r, RDF.li(i), o);
			} else if (o instanceof JSONObject) {
				transformJSONObject(m, r, RDF.li(i), (JSONObject) o);
			} else if (o instanceof JSONArray) {
				transformArray(m, r, RDF.li(i), (JSONArray) o);
			}
		}
		;
		return m;
	}

	private void transformArray(Model m, Resource r, Property p, JSONArray o) {
		Resource seq = createResource(r.getURI() + "/" + p.getLocalName());
		m.add(r, p, seq);
		m.add(getModel(o, seq));
	}

	private void transformJSONObject(Model m, Resource r, Property p, JSONObject o) {
		Resource rnew = createResource(r.getURI() + "/" + p.getLocalName());
		m.add(r, p, rnew);
		m.add(getModel(o, rnew));
	}

	private void transformPrimites(Model m, Resource r, Property p, Object o) {
		m.add(r, p, m.createTypedLiteral(o));
	}

	private Resource createResource(String path) {
		if (useBlankNodes) {
			return ModelFactory.createDefaultModel().createResource();
		} else {
			return ModelFactory.createDefaultModel().createResource(path);
		}

	}

	public void setPropertyPrefix(String propertyPrefix) {
		this.propertyPrefix = propertyPrefix;
	}

	public void setURIRoot(String uri) {
		if (uri != null) {
			this.uriRoot = uri;
			useBlankNodes = false;
		}
	}

	@Override
	public Graph triplify(URL url) throws IOException {
		Model m = transformJSONFromURL(url);
		Graph g = DatasetFactory.create(m).asDatasetGraph().getDefaultGraph();
		return g;
	}

	@Override
	public void setParameters(Properties properties) {
		if (properties.containsKey(PROPERTY_PREFIX_PARAMETER)) {
			propertyPrefix = properties.getProperty(PROPERTY_PREFIX_PARAMETER);
		}

		if (properties.containsKey(URI_ROOT_PARAMETER)) {
			this.setURIRoot(properties.getProperty(URI_ROOT_PARAMETER));
			logger.trace("Set uriRoot "+uriRoot);
		}

		if (properties.containsKey(USE_BLANK_NODES_PARAMETER)) {
			useBlankNodes = Boolean.parseBoolean(properties.getProperty(USE_BLANK_NODES_PARAMETER));
		}
	}

}
