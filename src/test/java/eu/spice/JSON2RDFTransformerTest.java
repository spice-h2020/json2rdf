package eu.spice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.json.JSONException;
import org.junit.Test;

import eu.spice.json2rdf.transformers.JSONTransformer;

public class JSON2RDFTransformerTest {

	private String ontologyPrefix = "https://w3id.org/resource/ontology/";
	private JSONTransformer jt = new JSONTransformer(ontologyPrefix);

	@Test
	public void testEmptyAndNull() {

		assertEquals(jt.transformJSON("{}").size(), 0L);
		assertEquals(jt.transformJSON("[]").size(), 1L);

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

	@Test
	public void testPrimitive() {
		String primitive1 = "{'a':1,'string':'string','bool':true,'n':null,'boolf':false}";
		{
			Model m = ModelFactory.createDefaultModel();
			Resource r = m.createResource();
			m.add(r, m.createProperty(ontologyPrefix + "a"), m.createTypedLiteral(1));
			m.add(r, m.createProperty(ontologyPrefix + "string"), m.createTypedLiteral("string"));
			m.add(r, m.createProperty(ontologyPrefix + "bool"), m.createTypedLiteral(true));
			m.add(r, m.createProperty(ontologyPrefix + "boolf"), m.createTypedLiteral(false));
			assertTrue(m.isIsomorphicWith(jt.transformJSON(primitive1)));
		}

		{
			JSONTransformer jtN = new JSONTransformer(ontologyPrefix);
			String reourcePrefix = "https://w3id.org/spice/resource/";
			jtN.setResourcePrefix(reourcePrefix);

			Model mn = ModelFactory.createDefaultModel();
			Resource rn = mn.createResource(reourcePrefix + JSONTransformer.ROOT_OBJECT_ID);
			mn.add(rn, mn.createProperty(ontologyPrefix + "a"), mn.createTypedLiteral(1));
			mn.add(rn, mn.createProperty(ontologyPrefix + "string"), mn.createTypedLiteral("string"));
			mn.add(rn, mn.createProperty(ontologyPrefix + "bool"), mn.createTypedLiteral(true));
			mn.add(rn, mn.createProperty(ontologyPrefix + "boolf"), mn.createTypedLiteral(false));
			assertTrue(mn.isIsomorphicWith(jtN.transformJSON(primitive1)));
		}
	}

	@Test
	public void testArray() {
		String array = "[1,'abcd',{'a':'a','arr':[0,1]},null,4]";
		{
			Model m = ModelFactory.createDefaultModel();
			Resource r = m.createResource();
			m.add(r, RDF.type, RDF.Seq);
			m.add(r, RDF.li(0), m.createTypedLiteral(1));
			m.add(r, RDF.li(1), m.createTypedLiteral("abcd"));
			Resource o = m.createResource();
			m.add(r, RDF.li(2), o);
			m.add(o, m.createProperty(ontologyPrefix + "a"), m.createTypedLiteral("a"));
			m.add(r, RDF.li(4), m.createTypedLiteral(4));
			Resource arr = m.createResource();
			m.add(o, m.createProperty(ontologyPrefix + "arr"), arr);
			m.add(arr, RDF.type, RDF.Seq);
			m.add(arr, RDF.li(0), m.createTypedLiteral(0));
			m.add(arr, RDF.li(1), m.createTypedLiteral(1));

			assertTrue(m.isIsomorphicWith(jt.transformJSON(array)));
		}

		{
			JSONTransformer jtN = new JSONTransformer(ontologyPrefix);
			String reourcePrefix = "https://w3id.org/spice/resource/";
			jtN.setResourcePrefix(reourcePrefix);

			Model m = ModelFactory.createDefaultModel();
			Resource r = m.createResource(reourcePrefix + JSONTransformer.ROOT_OBJECT_ID);
			m.add(r, RDF.type, RDF.Seq);
			m.add(r, RDF.li(0), m.createTypedLiteral(1));
			m.add(r, RDF.li(1), m.createTypedLiteral("abcd"));
			Resource o = m.createResource(reourcePrefix + JSONTransformer.ROOT_OBJECT_ID + "/_2");
			m.add(r, RDF.li(2), o);
			m.add(o, m.createProperty(ontologyPrefix + "a"), m.createTypedLiteral("a"));
			m.add(r, RDF.li(4), m.createTypedLiteral(4));
			Resource arr = m.createResource(reourcePrefix + JSONTransformer.ROOT_OBJECT_ID + "/_2/arr");
			m.add(o, m.createProperty(ontologyPrefix + "arr"), arr);
			m.add(arr, RDF.type, RDF.Seq);
			m.add(arr, RDF.li(0), m.createTypedLiteral(0));
			m.add(arr, RDF.li(1), m.createTypedLiteral(1));

			assertTrue(m.isIsomorphicWith(jtN.transformJSON(array)));
		}
	}

}
