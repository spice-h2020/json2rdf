package com.github.spiceh2020.json2rdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.github.spiceh2020.json2rdf.transformers.JSONTransformer;

public class JSON2RDF {

	public static void main(String[] args) {
		try {
			Options options = new Options();

			options.addOption(new Option("i", "input", true, "MANDATORY - The path to the JSON file to transform."));
			options.addOption(new Option("o", "output", true,
					"OPTIONAL - The path to the RDF file to be generated. [Default: STD-OUT]"));
			options.addOption(new Option("s", "syntax", true, "OPTIONAL - The RDF syntax of the output file. [Default: TTL]"));
			options.addOption(new Option("p", "prefix-for-properties", true,
					"OPTIONAL - The namespace prefix for the properties to be generated. [Default: https://w3id.org/spice/ontology/]"));
			options.addOption(new Option("r", "uri-root", true,
					"OPTIONAL - The URI of the root resource. [Default: https://w3id.org/spice/resource/root]"));
//			options.addOption(
//					new Option("n", "n", false, "OPTIONAL - Generate named resources instead of blank nodes."));
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);

			if (!cmd.hasOption("i")) {
				System.err.println("Input or prefix for properties not provided!");
				HelpFormatter formatter = new HelpFormatter();
				formatter.setWidth(1000);
				formatter.printHelp( "json2rdf", options );
				System.exit(0);
			}

			String input = cmd.getOptionValue("i");
			String prefix = cmd.getOptionValue("p", "https://w3id.org/spice/ontology/");
			String output = cmd.getOptionValue("o");
			String syntax = cmd.getOptionValue("s", "TTL");
			String resourcePrefix = cmd.getOptionValue("r", "https://w3id.org/spice/resource/root");

			JSONTransformer bnt = new JSONTransformer();
			if (prefix != null) {
				bnt.setPropertyPrefix(prefix);
			}

			if (resourcePrefix != null) {
				bnt.setURIRoot(resourcePrefix);
			}

			if (output != null) {
				bnt.transformJSONFile(new File(input)).write(new FileOutputStream(new File(output)), syntax);
			} else {
				bnt.transformJSONFile(new File(input)).write(System.out, syntax);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
