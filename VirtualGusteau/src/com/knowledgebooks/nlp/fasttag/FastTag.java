// Copyright 2003-2005.  Mark Watson (markw@markwatson.com).  All rights reserved.
// This software is released under the GPL (www.fsf.org)
// For an alternative non-GPL license: contact the author
// THIS SOFTWARE COMES WITH NO WARRANTY


package com.knowledgebooks.nlp.fasttag;

import java.io.*;
import java.util.*;


/**
 * <p/>
 * Copyright 2002-2007 by Mark Watson. All rights reserved.
 * <p/>
 * This software is not public domain. It can be legally
 * used under either of the following licenses:
 * <p/>
 * 1. KnowledgeBooks.com Non Commercial Royality Free License
 * 2. KnowledgeBooks.com Commercial Use License
 * <p/>
 * see www.knowledgebooks.com for details
 */
public class FastTag {

    public static Hashtable<String, String[]> lexicon = new Hashtable<String, String[]>(); // make private after debug

    static {
        System.out.println("Starting to load FastTag data...");
        try {
            System.out.println("Starting kbs.fasttag.FastTag static initialization...");
            InputStream ins = FastTag.class.getClassLoader().getResourceAsStream("lexicon.txt");
            if (ins == null) {
                ins = new FileInputStream("data/lexicon.txt");
            }
            if (ins == null) {
                System.out.println("Failed to open 'lexicon.txt'");
                System.exit(1);
            } else {
                Scanner scanner =
                        new Scanner(ins);
                scanner.useDelimiter
                        (System.getProperty("line.separator"));
                while (scanner.hasNext()) {
                    parseLine(scanner.next());
                }
                System.out.println(lexicon.size());
                scanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public FastTag() {
    }

    public boolean wordInLexicon(String word) {
        String[] ss = lexicon.get(word);
        if (ss != null) return true;
        // 1/22/2002 mod (from Lisp code): if not in hash, try lower case:
        if (ss == null)
            ss = lexicon.get(word.toLowerCase());
        if (ss != null) return true;
        return false;
    }

    public String[] tag(String[] words) {
        String[] ret = new String[words.length];
        for (int i = 0, size = words.length; i < size; i++) {
            String[] ss = (String[]) lexicon.get(words[i]);
            // 1/22/2002 mod (from Lisp code): if not in hash, try lower case:
            if (ss == null)
                ss = lexicon.get(words[i].toLowerCase());
            if (ss == null && words[i].length() == 1)
                ret[i] = words[i] + "^";
            if (ss == null)
                ret[i] = "NN";
            else
                ret[i] = ss[0];
        }
        /**
         * Apply transformational rules
         **/
        for (int i = 0; i < words.length; i++) {
            //  rule 1: DT, {VBD | VBP} --> DT, NN
            if (i > 0 && ret[i - 1].equals("DT")) {
                if (ret[i].equals("VBD")
                        || ret[i].equals("VBP")
                        || ret[i].equals("VB")) {
                    ret[i] = "NN";
                }
            }
            // rule 2: convert a noun to a number (CD) if "." appears in the word
            if (ret[i].startsWith("N")) {
                if (words[i].indexOf(".") > -1) {
                    ret[i] = "CD";
                }
                try {
                    Float.parseFloat(words[i]);
                    ret[i] = "CD";
                } catch (Exception e) {
                }
            }
            // rule 3: convert a noun to a past participle if words[i] ends with "ed"
            if (ret[i].startsWith("N") && words[i].endsWith("ed"))
                ret[i] = "VBN";
            // rule 4: convert any type to adverb if it ends in "ly";
            if (words[i].endsWith("ly"))
                ret[i] = "RB";
            // rule 5: convert a common noun (NN or NNS) to a adjective if it ends with "al"
            if (ret[i].startsWith("NN") && words[i].endsWith("al"))
                ret[i] = "JJ";
            // rule 6: convert a noun to a verb if the preceeding work is "would"
            if (i > 0
                    && ret[i].startsWith("NN")
                    && words[i - 1].equalsIgnoreCase("would"))
                ret[i] = "VB";
            // rule 7: if a word has been categorized as a common noun and it ends with "s",
            //         then set its type to plural common noun (NNS)
            if (ret[i].equals("NN") && words[i].endsWith("s"))
                ret[i] = "NNS";
            // rule 8: convert a common noun to a present participle verb (i.e., a gerand)
            if (ret[i].startsWith("NN") && words[i].endsWith("ing"))
                ret[i] = "VBG";
        }
        return ret;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: argument is a string like \"The ball rolled down the street.\"");
        } else {
            String[] words = com.knowledgebooks.nlp.util.Tokenizer.wordsToArray(args[0]);
            String[] tags = (new FastTag()).tag(words);
            for (int i = 0; i < words.length; i++) System.out.println(words[i] + "/" + tags[i]);
        }
    }

    private static void parseLine(String line) {
        int count = 0;
        for (int i=0, size=line.length(); i<size; i++) if (line.charAt(i)==' ') count++;
        if (count==0) return;
        String[] ss = new String[count];
        Scanner lineScanner = new Scanner(line);
        lineScanner.useDelimiter(" ");
        String word = lineScanner.next();    count=0;
        while (lineScanner.hasNext()) {
            ss[count++] = lineScanner.next();
        }
        lexicon.put(word, ss);
    }

}
