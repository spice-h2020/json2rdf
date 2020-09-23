# json2rdf

json2rdf implements a series of straightforward assumptions for serving the content of a JSON objects as RDF triples:

1. JSON objects maps to RDF resources that, depending on the configuration of json2rdf, can be either URIs or blank nodes.
2. Fields of the JSON objects maps to properties.
3. JSON arrays maps to [RDF Sequence](https://www.w3.org/TR/rdf-schema/#ch_seq).
4. JSON primitive types String, Number and Boolean maps to corresponding XSD types, ``xsd:string``, ``xsd:int`` and ``xsd:boolean``.

### Preliminaries

You can compile and execute json2rdf using maven, as follows

```
mvn clean install 
mvn exec:java -Dexec.mainClass="eu.spice.json2rdf.JSON2RDF" -Dexec.args="..."
```

### Usage

```
usage: json2rdf
 -i,--input <arg>                   MANDATORY - The path to the JSON file to transform.
 -o,--output <arg>                  OPTIONAL - The path to the RDF file to be generated. [Default: STD-OUT]
 -p,--prefix-for-properties <arg>   OPTIONAL - The namespace prefix for the properties to be generated. [Default: https://w3id.org/spice/ontology/]
 -r,--uri-root <arg>                OPTIONAL - The URI of the root resource. [Default: https://w3id.org/spice/resource/root]
 -s,--syntax <arg>                  OPTIONAL - The RDF syntax of the output file. [Default: TTL]
```

### Transformation Examples

A bunch of examples ..

#### Blank nodes

|Arguments|Input|Output|
|---|---|---|
|   |   |   |


