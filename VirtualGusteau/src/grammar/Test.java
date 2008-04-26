package grammar;

import java.util.LinkedList;

/**
 *
 * @author rkrantz
 */
public class Test {
    private LinkedList<Object> S;
    /**
     * S → NP VP
     *   | S Conjunction S
     * 
     * NP → Pronoun             → PRP
     *    | Noun                → NN
     *    | Article Noun        → DT NN
     *    | Digit               → CD
     *    | NP PP
     *    | NP RelClause
     * 
     * VP → Verb
     *    | VP NP
     *    | VP Adjective
     *    | VP PP
     *    | VP Adverb
     * 
     * PP → Preposition NP
     * RelClause → that VP
     * 
     * @param words
     * @param tags
     */
    public Test() {        
        S = new LinkedList<Object>();
    }
    public void grammar(String[] words, String[] tags) throws Exception {
        for(int i = 0; i < tags.length; i++) {
            if(tags[i].equals("PRP")) {
                if(S.isEmpty()) {
                    // The Pronoun is first in the sentence, add it
                    S.add(new NP(new PN(words[i])));
                } else if(S.getLast()instanceof VP) {
                    // Pronoun preceded by a Verb Phrase
                    //  » VP → VP NP
                    VP tmp = (VP)S.getLast();
                    S.removeLast();
                    S.add(new VP(tmp,new NP(new PN(words[i]))));
                } else if(S.getLast()instanceof CC) {
                    // Second part of sentence preceded by a Conjunction, i.e. and/but/or
                    // The Pronoun is first in second part, add it
                    S.add(new NP(new PN(words[i])));
                } else if(S.getLast()instanceof P) {
                    // Word before was a preposition, make a PP of it
                    P tmp = (P)S.getLast();
                    S.removeLast();
                    S.add(new PP(tmp,new NP(new PN(words[i]))));
                } else if(S.getLast()instanceof A) {
                    throw new Exception("Possible Illegal Sentence Structure - Article before Pronoun");
                } else {
                    throw new Exception("Illegal Sentence Structure - Pronoun in the wrong place");
                }
            } else if(tags[i].equals("DT")) {
                S.add(new A(words[i]));
            } else if(tags[i].contains("NN")) {
                if(S.isEmpty()) {
                    S.add(new NP(new N(words[i])));
                } else if(S.getLast()instanceof A) {
                    A tmp = (A)S.getLast();
                    S.removeLast();
                    S.add(new NP(tmp,new N(words[i])));
                } else if(S.getLast()instanceof VP) {
                    VP tmp = (VP)S.getLast();
                    S.removeLast();
                    S.add(new VP(tmp,new NP(new N(words[i]))));
                } else if(S.getLast()instanceof P) {
                    // Word before was a preposition, make a PP of it
                    P tmp = (P)S.getLast();
                    S.removeLast();
                    S.add(new PP(tmp,new NP(new PN(words[i])))); 
                } else if(S.getLast()instanceof CC) {
                    // Second part of sentence preceded by a Conjunction, i.e. and/but/or
                    // The Pronoun is first in second part, add it
                    S.add(new NP(new PN(words[i])));
                } else {
                    throw new Exception("Illegal Sentence Structure - Noun in the wrong place");
                }
            } else if(tags[i].equals("IN")) {
                if(S.getLast()instanceof NP) {
                    // NP preceding the preposition → correct
                    S.add(new P(words[i]));
                } else if(S.getLast()instanceof VP) {
                    // VP preceding the preposition → correct
                    S.add(new P(words[i]));
                } else {
                    throw new Exception("Illegal Sentence Structure - Preposition in the wrong place");
                }                
            } else if(tags[i].contains("VB")) {
                if(S.isEmpty()) {
                    throw new Exception("Illegal Sentence Structure - Verb cannot be first in sentence");
                } else if(S.getLast()instanceof NP) {
                    S.add(new VP(new V(words[i])));
                } else {
                    throw new Exception("Illegal Sentence Structure - Verb in the wrong place");
                }
            } else if(tags[i].contains("JJ")) {
                if(S.isEmpty()) {
                    throw new Exception("Illegal Sentence Structure - Adjective cannot be first in sentence");
                } else if(S.getLast()instanceof NP) {
                    throw new Exception("Illegal Sentence Structure - Adjective cannot follow NP");
                } else if(S.getLast()instanceof VP) {
                    VP tmp = (VP)S.getLast();
                    S.removeLast();
                    S.add(new VP(tmp, new JJ(words[i])));
                } else {
                    throw new Exception("Illegal Sentence Structure - Adjective in the wrong place");
                }
            } else if(tags[i].equals("CC")) {
                if(S.isEmpty()) {
                    throw new Exception("Illegal Sentence Structure - Conjunction cannot be first in sentence");
                } else if(S.getLast()instanceof VP && S.get(S.size()-2)instanceof NP) {
                    S.add(new CC(words[i]));
                } else {
                    throw new Exception("Illegal Sentence Structure");
                }
            } else if(tags[i].equals("RB")) {
                if(S.isEmpty()) {
                    throw new Exception("Illegal Sentence Structure - Adverb cannot be first in sentence");
                } else if(S.getLast()instanceof NP) {
                    throw new Exception("Illegal Sentence Structure - Adverb cannot follow Noun Phrase");
                } else if(S.getLast()instanceof VP) {
                    VP tmp = (VP)S.getLast();
                    S.removeLast();
                    S.add(new VP(tmp, new RB(words[i])));
                } else {
                    throw new Exception("Illegal Sentence Structure");
                }
            }
            if(S.getLast()instanceof NP) {
                // Sentence ends in NP → not correct, further checks needed
                if(S.size() > 2) {
                    // More than two Phrases in sentence
                    if(S.get(S.size()-2)instanceof VP) {
                        // If the second last Phrase is VP and last is NP, create a VP of them
                        NP np_tmp = (NP)S.getLast();
                        VP vp_tmp = (VP)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new VP(vp_tmp, np_tmp));
                    } else if(S.get(S.size()-2)instanceof P) {
                        NP np_tmp = (NP)S.getLast();
                        P p_tmp = (P)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new PP(p_tmp, np_tmp));
                    }
                }
            } else if(S.getLast()instanceof PP) {
                if(S.size() > 2) {
                    if(S.get(S.size()-2)instanceof NP) {
                        PP pp_tmp = (PP)S.getLast();
                        NP np_tmp = (NP)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new NP(np_tmp, pp_tmp));
                    } else if(S.get(S.size()-2)instanceof VP) {
                        PP pp_tmp = (PP)S.getLast();
                        VP vp_tmp = (VP)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new VP(vp_tmp, pp_tmp));
                    }
                }
            }
        }
        if(S.getLast()instanceof NP) {
                // Sentence ends in NP → not correct, further checks needed
                if(S.size() > 2) {
                    // More than two Phrases in sentence
                    if(S.get(S.size()-2)instanceof VP) {
                        // If the second last Phrase is VP and last is NP, create a VP of them
                        NP np_tmp = (NP)S.getLast();
                        VP vp_tmp = (VP)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new VP(vp_tmp, np_tmp));
                    } else if(S.get(S.size()-2)instanceof P) {
                        NP np_tmp = (NP)S.getLast();
                        P p_tmp = (P)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new PP(p_tmp, np_tmp));
                    }
                }
            } else if(S.getLast()instanceof PP) {
                if(S.size() > 2) {
                    if(S.get(S.size()-2)instanceof NP) {
                        PP pp_tmp = (PP)S.getLast();
                        NP np_tmp = (NP)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new NP(np_tmp, pp_tmp));
                    } else if(S.get(S.size()-2)instanceof VP) {
                        PP pp_tmp = (PP)S.getLast();
                        VP vp_tmp = (VP)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new VP(vp_tmp, pp_tmp));
                    }
                }
            }
        for(int i = 0; i < S.size(); i++) {
            System.out.print(S.get(i).toString() + " ");
        }
        System.out.println("\n-------------------------");
    }
}
