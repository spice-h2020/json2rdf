# json2rdf

json2rdf implements a series of straightforward assumptions for serving the content of a JSON objects as RDF triples:

1. JSON objects maps to RDF resources that, depending on the configuration of json2rdf, can be either URIs or blank nodes.
2. Fields of the JSON objects maps to properties.
3. JSON arrays maps to [RDF Sequence](https://www.w3.org/TR/rdf-schema/#ch_seq).
4. JSON primitive types String, Number and Boolean maps to corresponding XSD types, ``xsd:string``, ``xsd:int`` and ``xsd:boolean``.
5. By default, json2rdf maps JSON objects/arrays to blank nodes, however you can use ``--uri-root`` argument to map JSON objects/arrays to URIs. In this case the URI that you pass to json2rdf as argument for ``--uri-root`` will be the URI of the root JSON object/array. URIs for the nested JSON objects/arrays will be derivede by concatening the URI of the root and the JSON path of the JSON object/array.
6. json2rdf is meant to extract the content of a JSON file, therefore ``null`` values correspond to no triples (cf. [W3C discussion about this topic](https://lists.w3.org/Archives/Public/public-lod/2013Jun/0007.html)).

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

A bunch of examples of transformations 

|Arguments|Input File|Output|
|---|---|---|
|   |<pre>{<br>  "stringArg":"stringValue",<br>  "intArg":1,<br>  "booleanArg":true,<br>  "nullArg": null,<br>  "arr":[0,1]<br>}</pre>| <pre>@prefix ex:    &lt;https://w3id.org/spice/ontology/&gt; .<br><br>[ a              rdfs:Resource ;<br>  ex:arr         [ a       rdf:Seq ;<br>                   rdf:_0  "0"^^xsd:int ;<br>                   rdf:_1  "1"^^xsd:int<br>                 ] ;<br>  ex:booleanArg  true ;<br>  ex:intArg      "1"^^xsd:int ;<br>  ex:stringArg   "stringValue"<br>] .</pre> |
|   | <pre>["a",1,true,null,{"id":"a"}]</pre>  | <pre>@prefix ex:    &lt;https://w3id.org/spice/ontology/&gt; .<br><br>[ a       rdf:Seq ;<br>  rdf:_0  "a" ;<br>  rdf:_1  "1"^^xsd:int ;<br>  rdf:_2  true ;<br>  rdf:_4  [ a       rdfs:Resource ;<br>            ex:id   "a"<br>          ]<br>] .</pre>  |
|<pre>-r https://w3id.org/spice/resource/root</pre>|<pre>{<br>  "stringArg":"stringValue",<br>  "intArg":1,<br>  "booleanArg":true,<br>  "nullArg": null,<br>  "arr":[0,1]<br>}</pre>| <pre>@prefix ex:    &lt;https://w3id.org/spice/ontology/&gt; .<br><br>&lt;https://w3id.org/spice/resource/root&gt;<br>        a              rdfs:Resource ;<br>        ex:arr         &lt;https://w3id.org/spice/resource/root/arr&gt; ;<br>        ex:booleanArg  true ;<br>        ex:intArg      "1"^^xsd:int ;<br>        ex:stringArg   "stringValue" .<br><br>&lt;https://w3id.org/spice/resource/root/arr&gt;<br>        a       rdf:Seq ;<br>        rdf:_0  "0"^^xsd:int ;<br>        rdf:_1  "1"^^xsd:int .</pre> |

## License

json2rdf is licensed under [Apache 2.0](License).


