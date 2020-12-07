package com.github.spiceh2020.json2rdf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
//import org.apache.jena.vocabulary.RDFS;
import org.json.JSONException;
import org.junit.Test;

import com.github.spiceh2020.json2rdf.transformers.JSONTransformer;
import com.github.spiceh2020.json2rdf.transformers.JSONTransformer_JSONIter;

public class JSON2RDFTransformerTest {

	private String ontologyPrefix = "https://w3id.org/resource/ontology/";

	@Test
	public void testEmptyAndNull() {

		{
			JSONTransformer jt = new JSONTransformer(ontologyPrefix);

			assertEquals(jt.transformJSON("{}").size(), 0L);
			assertEquals(jt.transformJSON("[]").size(), 0L);

			boolean jsonException = false;
			try {
				jt.transformJSON("").size();
			} catch (JSONException e) {
				jsonException = true;
			}
			assertTrue(jsonException);

			boolean nullPointerException = false;
			try {
				jt.transformJSON(null).size();
			} catch (NullPointerException e) {
				nullPointerException = true;
			}
			assertTrue(nullPointerException);
		}
		{
			JSONTransformer_JSONIter jt = new JSONTransformer_JSONIter(ontologyPrefix);
			

			assertEquals(jt.transformJSON("{}").size(), 0L);
			assertEquals(jt.transformJSON("[]").size(), 0L);

			boolean jsonException = false;
			try {
				jt.transformJSON("").size();
			} catch (com.jsoniter.spi.JsonException e) {
				jsonException = true;
			}
			assertTrue(jsonException);

			boolean nullPointerException = false;
			try {
				jt.transformJSON(null).size();
			} catch (NullPointerException e) {
				nullPointerException = true;
			}
			assertTrue(nullPointerException);
		}

	}

	@Test
	public void testPrimitive() {
		{
			String primitive1 = "{'a':1,'string':'string','bool':true,'n':null,'boolf':false}";

			{
				JSONTransformer jt = new JSONTransformer(ontologyPrefix);
				Model m = ModelFactory.createDefaultModel();
				Resource r = m.createResource();
//				m.add(r, RDF.type, RDFS.Resource);
				m.add(r, m.createProperty(ontologyPrefix + "a"), m.createTypedLiteral(1));
				m.add(r, m.createProperty(ontologyPrefix + "string"), m.createTypedLiteral("string"));
				m.add(r, m.createProperty(ontologyPrefix + "bool"), m.createTypedLiteral(true));
				m.add(r, m.createProperty(ontologyPrefix + "boolf"), m.createTypedLiteral(false));
//				m.add(m.createProperty(ontologyPrefix + "a"), RDFS.label, m.createTypedLiteral("a"));
//				m.add(m.createProperty(ontologyPrefix + "string"), RDFS.label, m.createTypedLiteral("string"));
//				m.add(m.createProperty(ontologyPrefix + "bool"), RDFS.label, m.createTypedLiteral("bool"));
//				m.add(m.createProperty(ontologyPrefix + "boolf"), RDFS.label, m.createTypedLiteral("boolf"));
//				m.add(m.createProperty(ontologyPrefix + "n"), RDFS.label, m.createTypedLiteral("n"));
				assertTrue(m.isIsomorphicWith(jt.transformJSON(primitive1)));
			}

			{
				JSONTransformer jtN = new JSONTransformer(ontologyPrefix);
				String root = "https://w3id.org/spice/resource/root";
				jtN.setURIRoot(root);

				Model mn = ModelFactory.createDefaultModel();
				Resource rn = mn.createResource(root);
//				mn.add(rn, RDF.type, RDFS.Resource);
				mn.add(rn, mn.createProperty(ontologyPrefix + "a"), mn.createTypedLiteral(1));
				mn.add(rn, mn.createProperty(ontologyPrefix + "string"), mn.createTypedLiteral("string"));
				mn.add(rn, mn.createProperty(ontologyPrefix + "bool"), mn.createTypedLiteral(true));
				mn.add(rn, mn.createProperty(ontologyPrefix + "boolf"), mn.createTypedLiteral(false));
				
//				mn.add(mn.createProperty(ontologyPrefix + "a"), RDFS.label, mn.createTypedLiteral("a"));
//				mn.add(mn.createProperty(ontologyPrefix + "string"), RDFS.label, mn.createTypedLiteral("string"));
//				mn.add(mn.createProperty(ontologyPrefix + "bool"), RDFS.label, mn.createTypedLiteral("bool"));
//				mn.add(mn.createProperty(ontologyPrefix + "boolf"), RDFS.label, mn.createTypedLiteral("boolf"));
//				mn.add(mn.createProperty(ontologyPrefix + "n"), RDFS.label, mn.createTypedLiteral("n"));
				
				
				
				assertTrue(mn.isIsomorphicWith(jtN.transformJSON(primitive1)));
			}
		}

		{
			String primitive1 = "{\"a\":1,\"string\":\"string\",\"bool\":true,\"n\":null,\"boolf\":false}";

			{
				JSONTransformer_JSONIter jt = new JSONTransformer_JSONIter(ontologyPrefix);
				Model m = ModelFactory.createDefaultModel();
				Resource r = m.createResource();
				m.add(r, m.createProperty(ontologyPrefix + "a"), m.createTypedLiteral(1));
				m.add(r, m.createProperty(ontologyPrefix + "string"), m.createTypedLiteral("string"));
				m.add(r, m.createProperty(ontologyPrefix + "bool"), m.createTypedLiteral(true));
				m.add(r, m.createProperty(ontologyPrefix + "boolf"), m.createTypedLiteral(false));
				
//				m.add(r, RDF.type, RDFS.Resource);
//				m.add(m.createProperty(ontologyPrefix + "a"), RDFS.label, m.createTypedLiteral("a"));
//				m.add(m.createProperty(ontologyPrefix + "string"), RDFS.label, m.createTypedLiteral("string"));
//				m.add(m.createProperty(ontologyPrefix + "bool"), RDFS.label, m.createTypedLiteral("bool"));
//				m.add(m.createProperty(ontologyPrefix + "boolf"), RDFS.label, m.createTypedLiteral("boolf"));
//				m.add(m.createProperty(ontologyPrefix + "n"), RDFS.label, m.createTypedLiteral("n"));
				
				
				assertTrue(m.isIsomorphicWith(jt.transformJSON(primitive1)));
			}

			{
				JSONTransformer_JSONIter jtN = new JSONTransformer_JSONIter(ontologyPrefix);
				String root = "https://w3id.org/spice/resource/root";
				jtN.setURIRoot(root);

				Model mn = ModelFactory.createDefaultModel();
				Resource rn = mn.createResource(root);
//				mn.add(rn, RDF.type, RDFS.Resource);
				mn.add(rn, mn.createProperty(ontologyPrefix + "a"), mn.createTypedLiteral(1));
				mn.add(rn, mn.createProperty(ontologyPrefix + "string"), mn.createTypedLiteral("string"));
				mn.add(rn, mn.createProperty(ontologyPrefix + "bool"), mn.createTypedLiteral(true));
				mn.add(rn, mn.createProperty(ontologyPrefix + "boolf"), mn.createTypedLiteral(false));
				
//				mn.add(mn.createProperty(ontologyPrefix + "a"), RDFS.label, mn.createTypedLiteral("a"));
//				mn.add(mn.createProperty(ontologyPrefix + "string"), RDFS.label, mn.createTypedLiteral("string"));
//				mn.add(mn.createProperty(ontologyPrefix + "bool"), RDFS.label, mn.createTypedLiteral("bool"));
//				mn.add(mn.createProperty(ontologyPrefix + "boolf"), RDFS.label, mn.createTypedLiteral("boolf"));
//				mn.add(mn.createProperty(ontologyPrefix + "n"), RDFS.label, mn.createTypedLiteral("n"));
				
				
				assertTrue(mn.isIsomorphicWith(jtN.transformJSON(primitive1)));
			}
		}
	}

	@Test
	public void testArray() {
		{
			String array = "[1,'abcd',{'a':'a','arr':[0,1]},null,4]";
			{
				JSONTransformer jt = new JSONTransformer(ontologyPrefix);
				Model m = ModelFactory.createDefaultModel();
				Resource r = m.createResource();
//				m.add(r, RDF.type, RDF.Seq);
				m.add(r, RDF.li(1), m.createTypedLiteral(1));
				m.add(r, RDF.li(2), m.createTypedLiteral("abcd"));
				Resource o = m.createResource();
				m.add(r, RDF.li(3), o);
				m.add(o, m.createProperty(ontologyPrefix + "a"), m.createTypedLiteral("a"));
//				m.add(m.createProperty(ontologyPrefix + "a"), RDFS.label, m.createTypedLiteral("a"));
//				m.add(o, RDF.type, RDFS.Resource);
				m.add(r, RDF.li(5), m.createTypedLiteral(4));
				Resource arr = m.createResource();
				m.add(o, m.createProperty(ontologyPrefix + "arr"), arr);
//				m.add(m.createProperty(ontologyPrefix + "arr"), RDFS.label, m.createTypedLiteral("arr"));
//				m.add(arr, RDF.type, RDF.Seq);
				m.add(arr, RDF.li(1), m.createTypedLiteral(0));
				m.add(arr, RDF.li(2), m.createTypedLiteral(1));

				assertTrue(m.isIsomorphicWith(jt.transformJSON(array)));
			}

			{
				JSONTransformer jtN = new JSONTransformer(ontologyPrefix);
				String uriRoot = "https://w3id.org/spice/resource/root";
				jtN.setURIRoot(uriRoot);

				Model m = ModelFactory.createDefaultModel();
				Resource r = m.createResource(uriRoot);
//				m.add(r, RDF.type, RDF.Seq);
				m.add(r, RDF.li(1), m.createTypedLiteral(1));
				m.add(r, RDF.li(2), m.createTypedLiteral("abcd"));
				Resource o = m.createResource(uriRoot + "/_3");
				m.add(r, RDF.li(3), o);
//				m.add(o, RDF.type, RDFS.Resource);
				m.add(o, m.createProperty(ontologyPrefix + "a"), m.createTypedLiteral("a"));
//				m.add(m.createProperty(ontologyPrefix + "a"), RDFS.label, m.createTypedLiteral("a"));
				m.add(r, RDF.li(5), m.createTypedLiteral(4));
				Resource arr = m.createResource(uriRoot + "/_3/arr");
				m.add(o, m.createProperty(ontologyPrefix + "arr"), arr);
//				m.add(m.createProperty(ontologyPrefix + "arr"), RDFS.label, m.createTypedLiteral("arr"));
//				m.add(arr, RDF.type, RDF.Seq);
				m.add(arr, RDF.li(1), m.createTypedLiteral(0));
				m.add(arr, RDF.li(2), m.createTypedLiteral(1));
				assertTrue(m.isIsomorphicWith(jtN.transformJSON(array)));
			}
		}
		{
			String array = "[1,\"abcd\",{\"a\":\"a\",\"arr\":[0,1]},null,4]";
			{
				JSONTransformer_JSONIter jt = new JSONTransformer_JSONIter(ontologyPrefix);
				Model m = ModelFactory.createDefaultModel();
				Resource r = m.createResource();
//				m.add(r, RDF.type, RDF.Seq);
				m.add(r, RDF.li(1), m.createTypedLiteral(1));
				m.add(r, RDF.li(2), m.createTypedLiteral("abcd"));
				Resource o = m.createResource();
				m.add(r, RDF.li(3), o);
				m.add(o, m.createProperty(ontologyPrefix + "a"), m.createTypedLiteral("a"));
//				m.add(m.createProperty(ontologyPrefix + "a"), RDFS.label, m.createTypedLiteral("a"));
//				m.add(o, RDF.type, RDFS.Resource);
				m.add(r, RDF.li(5), m.createTypedLiteral(4));
				Resource arr = m.createResource();
				m.add(o, m.createProperty(ontologyPrefix + "arr"), arr);
//				m.add(m.createProperty(ontologyPrefix + "arr"), RDFS.label, m.createTypedLiteral("arr"));
//				m.add(arr, RDF.type, RDF.Seq);
				m.add(arr, RDF.li(1), m.createTypedLiteral(0));
				m.add(arr, RDF.li(2), m.createTypedLiteral(1));

				assertTrue(m.isIsomorphicWith(jt.transformJSON(array)));
			}

			{
				JSONTransformer_JSONIter jtN = new JSONTransformer_JSONIter(ontologyPrefix);
				String uriRoot = "https://w3id.org/spice/resource/root";
				jtN.setURIRoot(uriRoot);

				Model m = ModelFactory.createDefaultModel();
				Resource r = m.createResource(uriRoot);
//				m.add(r, RDF.type, RDF.Seq);
				m.add(r, RDF.li(1), m.createTypedLiteral(1));
				m.add(r, RDF.li(2), m.createTypedLiteral("abcd"));
				Resource o = m.createResource(uriRoot + "/3");
				m.add(r, RDF.li(3), o);
//				m.add(o, RDF.type, RDFS.Resource);
				m.add(o, m.createProperty(ontologyPrefix + "a"), m.createTypedLiteral("a"));
//				m.add(m.createProperty(ontologyPrefix + "a"), RDFS.label, m.createTypedLiteral("a"));
				m.add(r, RDF.li(5), m.createTypedLiteral(4));
				Resource arr = m.createResource(uriRoot + "/3/arr");
				m.add(o, m.createProperty(ontologyPrefix + "arr"), arr);
//				m.add(m.createProperty(ontologyPrefix + "arr"), RDFS.label, m.createTypedLiteral("arr"));
//				m.add(arr, RDF.type, RDF.Seq);
				m.add(arr, RDF.li(1), m.createTypedLiteral(0));
				m.add(arr, RDF.li(2), m.createTypedLiteral(1));
				assertTrue(m.isIsomorphicWith(jtN.transformJSON(array)));
			}
		}
	}

	@Test(expected = org.json.JSONException.class)
	public void testDuplicateKeyJSON() {
		{
			String test = "{\"a\":\"a\",\"a\":\"c\"}";
			JSONTransformer jt = new JSONTransformer(ontologyPrefix);
			jt.transformJSON(test);
		}
		{
			String test = "{\"a\":\"a\",\"a\":\"c\"}";
			JSONTransformer_JSONIter jt = new JSONTransformer_JSONIter(ontologyPrefix);
			jt.transformJSON(test);
		}
	}

}
