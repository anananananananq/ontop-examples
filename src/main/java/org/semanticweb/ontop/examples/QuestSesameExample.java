package org.semanticweb.ontop.examples;

/*
 * #%L
 * ontop-quest-owlapi3
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.openrdf.query.Binding;
import org.openrdf.query.Query;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import sesameWrapper.RepositoryConnection;
import sesameWrapper.SesameVirtualRepo;

import java.io.BufferedReader;
import java.io.FileReader;

public class QuestSesameExample {

    /*
     * Use the sample database using H2 from
     * https://github.com/ontop/ontop/wiki/InstallingTutorialDatabases
     *
     * Please use the pre-bundled H2 server from the above link
     *
     */
    final String owlfile = "src/main/resources/example/exampleBooks.owl";
    final String obdafile = "src/main/resources/example/exampleBooks.obda";
    final String sparqlFile = "src/main/resources/example/q1.rq";

    public void runQuery() throws Exception {
        String queryString = "";

        BufferedReader br = new BufferedReader(new FileReader(sparqlFile));
        String line;
        while ((line = br.readLine()) != null) {
            queryString += line + "\n";
        }


        System.out.println();
        System.out.println("The input SPARQL query:");
        System.out.println("=======================");
        System.out.println(queryString);
        System.out.println();


        // create and initialize repo
        boolean existential = false;
        String rewriting = "TreeWitness";
        SesameVirtualRepo repo = new SesameVirtualRepo("test_repo", owlfile, obdafile, existential, rewriting);

        repo.initialize();

        RepositoryConnection conn = repo.getConnection();

        // execute query
        Query query = conn.prepareQuery(QueryLanguage.SPARQL, queryString);

        TupleQuery tq = (TupleQuery) query;

        TupleQueryResult result = tq.evaluate();

        while (result.hasNext()) {
            for (Binding binding : result.next()) {
                System.out.print(binding.getValue() + ", ");
            }
            System.out.println();
        }

    }

    /**
     * Main client program
     */
    public static void main(String[] args) {
        try {
            QuestSesameExample example = new QuestSesameExample();
            example.runQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
