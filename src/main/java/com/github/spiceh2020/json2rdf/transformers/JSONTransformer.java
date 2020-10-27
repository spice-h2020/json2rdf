package com.github.spiceh2020.json2rdf.transformers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONTransformer {

	private String propertyPrefix, uriRoot;
	private boolean useBlankNodes = true;
	public static final String ROOT_OBJECT_ID = "root";

	public JSONTransformer(String propertyPrefix) {
		super();
		this.propertyPrefix = propertyPrefix;
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

		StringBuilder sb = new StringBuilder();
		br.lines().forEachOrdered(l -> sb.append(l));
		br.close();

		return transformJSON(sb.toString());
	}

	public Model transformJSON(String json) {
		try {
			return getModel(new JSONObject(json));
		} catch (JSONException e) {
			return getModel(new JSONArray(json));
		}
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

}
