package grammar;

import java.util.LinkedList;

/**
 *
 * @author rkrantz
 */
public class Test {
    private LinkedList<Object> S;
    /**
     * S → NounPhrase VerbPhrase
     *   | S Conjunction S
     * 
     * NounPhrase → Pronoun                 → PRP
     *    | Noun                    → NN
     *    | Article Noun            → DT NN
     *    | Adjective Noun          → Adjective NN
     *    | Article NounPhrase              → DT NounPhrase
     *    | Digit                   → Digit
     *    | NounPhrase PrepositionalPhrase
     *    | NounPhrase RelClause
     * 
     * VerbPhrase → Verb
     *    | VerbPhrase NounPhrase
     *    | VerbPhrase Adjective
     *    | VerbPhrase PrepositionalPhrase
     *    | VerbPhrase Adverb
     * 
     * PrepositionalPhrase → Preposition NounPhrase
     * RelClause → that VerbPhrase
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
                    S.add(new NounPhrase(new Pronoun(words[i])));
                } else if(S.getLast()instanceof VerbPhrase) {
                    // Pronoun preceded by a Verb Phrase
                    //  » VerbPhrase → VerbPhrase NounPhrase
                    VerbPhrase tmp = (VerbPhrase)S.getLast();
                    S.removeLast();
                    S.add(new VerbPhrase(tmp,new NounPhrase(new Pronoun(words[i]))));
                } else if(S.getLast()instanceof Conjunction) {
                    // Second part of sentence preceded by a Conjunction, i.e. and/but/or
                    // The Pronoun is first in second part, add it
                    S.add(new NounPhrase(new Pronoun(words[i])));
                } else if(S.getLast()instanceof Preposition) {
                    // Word before was a preposition, make a PrepositionalPhrase of it
                    Preposition tmp = (Preposition)S.getLast();
                    S.removeLast();
                    S.add(new PrepositionalPhrase(tmp,new NounPhrase(new Pronoun(words[i]))));
                } else if(S.getLast()instanceof Article) {
                    throw new Exception("Possible Illegal Sentence Structure - Article before Pronoun");
                } else {
                    throw new Exception("Illegal Sentence Structure - Pronoun in the wrong place");
                }
            } else if(tags[i].equals("DT")) {
                S.add(new Article(words[i]));
            } else if(tags[i].contains("NN")) {
                if(S.isEmpty()) {
                    S.add(new NounPhrase(new Noun(words[i])));
                } else if(S.getLast()instanceof Article) {
                    Article tmp = (Article)S.getLast();
                    S.removeLast();
                    S.add(new NounPhrase(tmp,new Noun(words[i])));
                } else if(S.getLast()instanceof VerbPhrase) {
                    VerbPhrase tmp = (VerbPhrase)S.getLast();
                    S.removeLast();
                    S.add(new VerbPhrase(tmp,new NounPhrase(new Noun(words[i]))));
                } else if(S.getLast()instanceof Preposition) {
                    // Word before was a preposition, make a PrepositionalPhrase of it
                    Preposition tmp = (Preposition)S.getLast();
                    S.removeLast();
                    S.add(new PrepositionalPhrase(tmp,new NounPhrase(new Noun(words[i])))); 
                } else if(S.getLast()instanceof Conjunction) {
                    // Second part of sentence preceded by a Conjunction, i.e. and/but/or
                    // The Pronoun is first in second part, add it
                    S.add(new NounPhrase(new Pronoun(words[i])));
                } else if(S.getLast()instanceof Adjective) {
                    // Adjective Noun
                    Adjective j_tmp = (Adjective)S.getLast();
                    S.removeLast();
                    //S.add(new NounPhrase(j_tmp, new Noun(words[i])));
                } else {
                    throw new Exception("Illegal Sentence Structure - Noun in the wrong place");
                }
            } else if(tags[i].equals("IN")) {
                if(words[i].toLowerCase().equals("that") && S.getLast() instanceof NounPhrase) {
                    // The beginning of a relClause following a NounPhrase
                    S.add(new Preposition(words[i]));
                } else {
                    if(S.getLast()instanceof NounPhrase) {
                    // NounPhrase preceding the preposition → correct
                    S.add(new Preposition(words[i]));
                    } else if(S.getLast()instanceof VerbPhrase) {
                        // VerbPhrase preceding the preposition → correct
                        S.add(new Preposition(words[i]));
                    } else {
                        throw new Exception("Illegal Sentence Structure - Preposition in the wrong place");
                    }                
                }
            } else if(tags[i].contains("VB") || tags[i].equals("MD")) {
                if(S.isEmpty()) {
                    throw new Exception("Illegal Sentence Structure - Verb cannot be first in sentence");
                } else if(words[i].toLowerCase().equals("do")) {
                    /**
                     * The word 'do' will appear in the context of "I do not want".
                     * This parser cannot handle auxiliary verbs at the moment and 
                     * the sentence is therefor illegal. Article solution to this is to 
                     * remove 'do' and switch place of 'not' and 'want', forming 
                     * the sentence "I want not" which is, according to this parser, 
                     * correct.
                     */
                    String wtmp = words[i+1];
                    String ttmp = tags[i+1];
                    words[i+1] = words[i+2];
                    tags[i+1] = tags[i+2];
                    words[i+2] = wtmp;
                    tags[i+2] = ttmp;
                                        
                } else if(S.getLast()instanceof NounPhrase) {
                    S.add(new VerbPhrase(new Verb(words[i])));
                } else if(S.getLast()instanceof Preposition && ((Preposition)S.getLast()).getPreposition().equals("that")) {
                    S.add(new VerbPhrase(new Verb(words[i])));
                } else {
                    throw new Exception("Illegal Sentence Structure - Verb in the wrong place");
                }
            } else if(tags[i].contains("JJ")) {
                if(S.isEmpty()) {
                    throw new Exception("Illegal Sentence Structure - Adjective cannot be first in sentence");
                } else if(S.getLast()instanceof NounPhrase) {
                    throw new Exception("Illegal Sentence Structure - Adjective cannot follow NP");
                } else if(S.getLast()instanceof VerbPhrase) {
                    VerbPhrase tmp = (VerbPhrase)S.getLast();
                    S.removeLast();
                    S.add(new VerbPhrase(tmp, new Adjective(words[i])));
                } else if(S.getLast()instanceof Article) {
                    // Article Adjective (Noun)
                    S.add(new Adjective(words[i]));
                } else {
                    throw new Exception("Illegal Sentence Structure - Adjective in the wrong place");
                }
            } else if(tags[i].equals("CC")) {
                if(S.getLast()instanceof NounPhrase) {
                    // Sentence ends in NounPhrase → not correct, further checks needed
                    if(S.size() > 2) {
                        // More than two Phrases in sentence
                        if(S.get(S.size()-2)instanceof VerbPhrase) {
                            // If the second last Phrase is VerbPhrase and last is NounPhrase, create a VerbPhrase of them
                            NounPhrase np_tmp = (NounPhrase)S.getLast();
                            VerbPhrase vp_tmp = (VerbPhrase)S.get(S.size()-2);
                            S.removeLast(); S.removeLast();
                            S.add(new VerbPhrase(vp_tmp, np_tmp));
                        } else if(S.get(S.size()-2)instanceof Preposition) {
                            NounPhrase np_tmp = (NounPhrase)S.getLast();
                            Preposition p_tmp = (Preposition)S.get(S.size()-2);
                            S.removeLast(); S.removeLast();
                            S.add(new PrepositionalPhrase(p_tmp, np_tmp));
                        }
                    }
                } else if(S.getLast()instanceof PrepositionalPhrase) {
                    if(S.size() > 2) {
                        if(S.get(S.size()-2)instanceof NounPhrase) {
                            PrepositionalPhrase pp_tmp = (PrepositionalPhrase)S.getLast();
                            NounPhrase np_tmp = (NounPhrase)S.get(S.size()-2);
                            S.removeLast(); S.removeLast();
                            S.add(new NounPhrase(np_tmp, pp_tmp));
                        } else if(S.get(S.size()-2)instanceof VerbPhrase) {
                            PrepositionalPhrase pp_tmp = (PrepositionalPhrase)S.getLast();
                            VerbPhrase vp_tmp = (VerbPhrase)S.get(S.size()-2);
                            S.removeLast(); S.removeLast();
                            S.add(new VerbPhrase(vp_tmp, pp_tmp));
                        }
                    }
                }
                if(S.isEmpty()) {
                    throw new Exception("Illegal Sentence Structure - Conjunction cannot be first in sentence");
                } else if(S.getLast()instanceof VerbPhrase && S.get(S.size()-2)instanceof NounPhrase) {
                    S.add(new Conjunction(words[i]));
                } else {
                    throw new Exception("Illegal Sentence Structure - CC");
                }
            } else if(tags[i].equals("RB")) {
                if(S.isEmpty()) {
                    throw new Exception("Illegal Sentence Structure - Adverb cannot be first in sentence");
                } else if(S.getLast()instanceof NounPhrase) {
                    throw new Exception("Illegal Sentence Structure - Adverb cannot follow Noun Phrase");
                } else if(S.getLast()instanceof VerbPhrase) {
                    VerbPhrase tmp = (VerbPhrase)S.getLast();
                    S.removeLast();
                    S.add(new VerbPhrase(tmp, new Adverb(words[i])));
                } else {
                    throw new Exception("Illegal Sentence Structure - RB");
                }
            } else if(tags[i].equals("CD")) {
                // Word is a number - treat as NounPhrase
                if(S.isEmpty()) {
                    // Legal or not?
                } else if(S.getLast()instanceof NounPhrase) {
                    throw new Exception("Illegal Sentence Structure - Digit cannot follow NP");
                } else if(S.getLast()instanceof VerbPhrase) {
                    VerbPhrase tmp = (VerbPhrase)S.getLast();
                    S.removeLast();
                    S.add(new VerbPhrase(tmp, new NounPhrase(new Digit(words[i]))));
                } else if(S.getLast()instanceof Preposition) {
                    Preposition tmp = (Preposition)S.getLast();
                    S.removeLast();
                    S.add(new PrepositionalPhrase(tmp, new NounPhrase(new Digit(words[i]))));
                } else {
                    throw new Exception("Illegal Sentence Structure - CD");
                }
            }
            if(S.getLast()instanceof NounPhrase) {
                // Sentence ends in NounPhrase → not correct, further checks needed
                if(S.size() > 2) {
                    // More than two Phrases in sentence
                    if(S.get(S.size()-2)instanceof VerbPhrase) {
                        // If the second last Phrase is VerbPhrase and last is NounPhrase, create a VerbPhrase of them
                        NounPhrase np_tmp = (NounPhrase)S.getLast();
                        VerbPhrase vp_tmp = (VerbPhrase)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new VerbPhrase(vp_tmp, np_tmp));
                    } else if(S.get(S.size()-2)instanceof Preposition) {
                        NounPhrase np_tmp = (NounPhrase)S.getLast();
                        Preposition p_tmp = (Preposition)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new PrepositionalPhrase(p_tmp, np_tmp));
                    } else if(S.get(S.size()-2)instanceof Article) {
                        NounPhrase np_tmp = (NounPhrase)S.getLast();
                        Article a_tmp = (Article)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new NounPhrase(a_tmp, np_tmp));
                    }
                }
            } else if(S.getLast()instanceof PrepositionalPhrase) {
                // Sentence ends in PrepositionalPhrase → not correct, further checks needed
                if(S.size() > 2) {
                    if(S.get(S.size()-2)instanceof NounPhrase) {
                        PrepositionalPhrase pp_tmp = (PrepositionalPhrase)S.getLast();
                        NounPhrase np_tmp = (NounPhrase)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new NounPhrase(np_tmp, pp_tmp));
                    } else if(S.get(S.size()-2)instanceof VerbPhrase) {
                        PrepositionalPhrase pp_tmp = (PrepositionalPhrase)S.getLast();
                        VerbPhrase vp_tmp = (VerbPhrase)S.get(S.size()-2);
                        S.removeLast(); S.removeLast();
                        S.add(new VerbPhrase(vp_tmp, pp_tmp));
                    }
                }
            }
        }
        if(S.getLast()instanceof NounPhrase) {
            // Sentence ends in NounPhrase → not correct, further checks needed
            if(S.size() > 2) {
                // More than two Phrases in sentence
                if(S.get(S.size()-2)instanceof VerbPhrase) {
                    // If the second last Phrase is VerbPhrase and last is NounPhrase, create a VerbPhrase of them
                    NounPhrase np_tmp = (NounPhrase)S.getLast();
                    VerbPhrase vp_tmp = (VerbPhrase)S.get(S.size()-2);
                    S.removeLast(); S.removeLast();
                    S.add(new VerbPhrase(vp_tmp, np_tmp));
                } else if(S.get(S.size()-2)instanceof Preposition) {
                    NounPhrase np_tmp = (NounPhrase)S.getLast();
                    Preposition p_tmp = (Preposition)S.get(S.size()-2);
                    S.removeLast(); S.removeLast();
                    S.add(new PrepositionalPhrase(p_tmp, np_tmp));
                }
            }
        } else if(S.getLast()instanceof PrepositionalPhrase) {
            if(S.size() >= 2) {
                if(S.get(S.size()-2)instanceof NounPhrase) {
                    PrepositionalPhrase pp_tmp = (PrepositionalPhrase)S.getLast();
                    NounPhrase np_tmp = (NounPhrase)S.get(S.size()-2);
                    S.removeLast(); S.removeLast();
                    S.add(new NounPhrase(np_tmp, pp_tmp));
                } else if(S.get(S.size()-2)instanceof VerbPhrase) {
                    PrepositionalPhrase pp_tmp = (PrepositionalPhrase)S.getLast();
                    VerbPhrase vp_tmp = (VerbPhrase)S.get(S.size()-2);
                    S.removeLast(); S.removeLast();
                    S.add(new VerbPhrase(vp_tmp, pp_tmp));
                }
            }
        } else if(S.getLast()instanceof VerbPhrase && S.get(S.size()-2)instanceof Preposition) {
            VerbPhrase vp_tmp = (VerbPhrase)S.getLast();            
            S.removeLast();
            Preposition p_tmp = (Preposition)S.getLast();
            S.removeLast();
            S.add(new RelClause(p_tmp, vp_tmp));
        }
        
        for(int i = 0; i < S.size(); i++) {
            System.out.print(S.get(i).toString() + " ");
        }
        System.out.println("\n-------------------------");
         
        GtFO gtfo = new GtFO();
        //gtfo.converter(S);

    }
}
