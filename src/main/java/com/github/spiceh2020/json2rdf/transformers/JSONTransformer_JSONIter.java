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

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;

public class JSONTransformer_JSONIter {

	private String propertyPrefix, uriRoot;
	private boolean useBlankNodes = true;

	public JSONTransformer_JSONIter(String propertyPrefix) {
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

		if (propertyPrefix == null) {
			propertyPrefix = url.toString() + "/";
		}
		StringBuilder sb = new StringBuilder();
		br.lines().forEachOrdered(l -> {
			sb.append(l);
			sb.append('\n');
		});
		br.close();

		return transformJSON(sb.toString());
	}

	public Model transformJSON(String json) {
		checkParameters();
		Any any = JsonIterator.deserialize(json);
		return getModel(any);
	}

	private void checkParameters() {
		if (propertyPrefix == null)
			throw new RuntimeException("The property prefix can't be null");
	}

	public Model getModel(Any any) {
		Model m = ModelFactory.createDefaultModel();
		transformAny(any, m, createResource(uriRoot));
		return m;
	}

	private void transformAny(Any object, Model m, Resource r) {
		if (object.valueType().equals(ValueType.OBJECT)) {
			m.add(r, RDF.type, RDFS.Resource);
			object.keys().spliterator().forEachRemaining(k -> {
				Any o = object.get(k);
				Property p = m.createProperty(propertyPrefix + k);
				m.add(p, RDFS.label, m.createTypedLiteral(k));
				if (o.valueType().equals(ValueType.ARRAY) || o.valueType().equals(ValueType.OBJECT)) {
					Resource rnew = createResource(r.getURI() + "/" + k);
					m.add(r, p, rnew);
					transformAny(o, m, rnew);
				} else {
					transformPrimitives(m, r, p, o);
				}
			});
		} else if (object.valueType().equals(ValueType.ARRAY)) {
			m.add(r, RDF.type, RDF.Seq);
			for (int i = 0; i < object.size(); i++) {
				Any o = object.get(i);
				Property p = RDF.li(i + 1);
				if (o.valueType().equals(ValueType.ARRAY) || o.valueType().equals(ValueType.OBJECT)) {
					Resource rnew = createResource(r.getURI() + "/" + (i + 1));
					m.add(r, p, rnew);
					transformAny(o, m, rnew);
				} else {
					transformPrimitives(m, r, p, o);
				}
			}
			;
		}

	}

	private void transformPrimitives(Model m, Resource r, Property p, Any o) {

		if (o.valueType().equals(ValueType.STRING)) {
			m.add(r, p, m.createTypedLiteral(o.toString()));
		} else if (o.valueType().equals(ValueType.NUMBER)) {
			m.add(r, p, m.createTypedLiteral(o.toInt()));
		} else if (o.valueType().equals(ValueType.BOOLEAN)) {
			m.add(r, p, m.createTypedLiteral(o.toBoolean()));
		}

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
