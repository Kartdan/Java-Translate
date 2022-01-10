import java.util.*;

public class Methods
{
    ArrayList<Combined> dictionar;
    public Methods(ArrayList<Combined> dictionar)
    {
        this.dictionar = dictionar;
    }

    boolean addWord(Word word, String language)
    {
        //verific daca nu exista deja cuvantul
        for(Combined contor : dictionar)
        {
            if(contor.word.getWord().equals(word.getWord()) && contor.language.equals(language))
            {
               return false;
            }
        }
        //daca nu exista, il adaug la final
        Combined buffer = new Combined();
        buffer.word = word;
        buffer.language = language;
        dictionar.add(buffer);
        return true;
    }

    boolean removeWord(String word, String language)
    {
        //daca gasesc cuvantul respectiv, il scot din dictionar, altfel returnez false
        for (Combined contor : dictionar)
        {
            if(contor.word.getWord().equals(word) && contor.language.equals(language))
            {
                dictionar.remove(contor);
                return true;
            }
        }
        return false;
    }

    boolean addDefinitionForWord(String word, String language, Definition definition)
    {
        //parcurg cuvintele din dictionar
        for(Combined contor: dictionar)
        {
            //daca gasesc cuvantul
            if(contor.word.getWord().equals(word) && contor.language.equals(language))
            {
                //ii parcurg definitiile
                for(Definition contor_def : contor.definition)
                {
                    //daca gasesc acelasi dictionar, returnez false
                    if(contor_def.dict.equals(definition.dict))
                    {
                        System.out.println("Exista deja aceasta definitie!");
                        return false;
                    }
                }
                //altfel, il adaug in lista de definitii
                contor.definition.add(definition);
                return true;
            }
        }
        return true;
    }

    boolean removeDefinition(String word, String language, String dictionary)
    {
        //parcurg cuvintele din dictionar
        for(Combined contor: dictionar)
        {
            if(contor.word.getWord().equals(word) && contor.language.equals(language))
            {
                //daca am o singura definitie, si este cea primita ca parametru, va trebui
                //sa sterg si cuvantul respectiv
                if(contor.definition.size() == 1)
                {
                    dictionar.remove(contor);
                    return true;
                }
                //altfel, parcurg definitiile, si daca o gasesc pe cea care trebuie, o scot
                //si returnez true
                for(Definition contor_def : contor.definition)
                {
                    if(contor_def.dict.equals(dictionary))
                    {
                        contor.definition.remove(contor_def);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    String translateWord(String word, String fromLanguage, String toLanguage)
    {
        String buffer_en = null;
        String return_word = null;
        //caut cuvantul pe care trebuie sa il traduc, si ii salvez forma lui
        //din limba engleza
        for(Combined contor : dictionar)
        {
            if(contor.language.equals(fromLanguage) && contor.word.getWord().equals(word))
            {
                buffer_en = contor.word.getWord_en();
                break;
            }
        }
        //caut cuvantul din limba engleza, pe care trebuie sa il traduc in limba
        //primita ca parametru
        for(Combined contor : dictionar)
        {
            if(contor.language.equals(toLanguage) && contor.word.getWord_en().equals(buffer_en))
            {
                return_word = contor.word.getWord();
                break;
            }
        }
        //daca nu ii gasesc traducerea, returnez un mesaj de eroare
        if(return_word == null)
        {
            return_word = "Could not translate the word!";
        }
        return return_word;
    }

    String translateSentence(String sentence, String fromLanguage, String toLanguage)
    {
        //imi iau cuvintele din propozitia pe care trebuie sa o traduc
        String[] all_words = sentence.split(" ");
        StringBuilder prop = new StringBuilder();
        int index = 0;
        String word_en = "";
        String buffer = "";
        int sg_pl = 0;
        int OK, check;
        //parcurg cuvintele ce trebuie traduse
        for(String words : all_words)
        {
            check = 0;
            OK = 0;
            //caut cuvant cu cuvant
            for(Combined contor : dictionar)
            {
                if(contor.language.equals(fromLanguage))
                {
                    int j = 0;
                    //ii caut forma, mai intai, in lista de forme de singular,
                    //si daca il gasesc, ii salvez forma initiala, forma de limba engleza,
                    //indexul din lista de forme de singular, si cu sg_pl 0, daca este
                    //in lista de singular, si cu 1 daca este in lista de plural. In OK
                    //salvez daca am gasit cuvantul respectiv
                    for(String sg : contor.word.singular)
                    {
                        String[] sg_word = sg.split("\"");
                        if(words.equals(sg_word[1]))
                        {
                            word_en = contor.word.getWord_en();
                            index = j;
                            OK = 1;
                            buffer = contor.word.getWord();
                            sg_pl = 0;
                            break;
                        }
                        j++;
                    }
                    j = 0;
                    if(OK == 0)
                    {
                        //analog si pentru lista de forme de plural
                        for (String pl : contor.word.plural)
                        {
                            String[] pl_word = pl.split("\"");
                            if (words.equals(pl_word[1]))
                            {
                                word_en = contor.word.getWord_en();
                                index = j;
                                OK = 1;
                                buffer = contor.word.getWord();
                                sg_pl = 1;
                                break;
                            }
                        }
                    }
                }
            }
            //daca exista cuvantul
            if(OK == 1)
            {
                for (Combined contor : dictionar)
                {
                    //in check salvez daca exista o traducere pentru cuvantul respectiv
                    //daca nu exista, il pun sub forma initiala
                    if (word_en.equals(contor.word.getWord_en()) && contor.language.equals(toLanguage))
                    {
                        check = 1;
                        String[] final_parts;
                        //verific daca forma cuvantului se afla in lista forme de singular
                        //sau de plural
                        if (sg_pl == 0)
                        {
                            final_parts = contor.word.singular.get(index).split("\"");
                        } else
                        {
                            final_parts = contor.word.plural.get(index).split("\"");
                        }
                        prop.append(final_parts[1]).append(" ");
                    }
                }
            }
            if(check == 0)
            {
                prop.append(buffer).append(" ");
            }
            //daca nu gasesc un cuvant din propozitia initiala ce trebuie tradus,
            //returnez un mesaj de eroare
            if(OK == 0)
            {
                return "Nu s-a putut traduce propozitia";
            }
        }

        return prop.toString();
    }

    ArrayList<Definition> getDefinitionsForWord(String word, String language)
    {
        ArrayList<Definition> returndef = new ArrayList<>();
        Combined find_word = new Combined();
        int OK = 0;
        //caut cuvantul respectiv, sa verific daca exista
        for(Combined contor : dictionar)
        {
            if(contor.language.equals(language) && contor.word.getWord().equals(word))
            {
                OK = 1;
                find_word = contor;
            }
        }
        //daca nu exista, returnez un mesaj de eroare
        if(OK == 0)
        {
            System.out.println("Nu se pot obtine definitiile!");
            return null;
        }
        int len = find_word.definition.size();
        Definition[] buffer = new Definition[len];
        //imi stochez definitiile intr-un vector
        for(int i = 0; i < len; i++)
        {
            buffer[i] = find_word.definition.get(i);
        }
        Definition aux;
        //sortez definitiile din vector dupa an
        for(int i = 0; i < len - 1; i++)
        {
            for(int j = 1; j < len; j++)
            {
                if(buffer[i].year > buffer[j].year)
                {
                    aux = buffer[i];
                    buffer[i] = buffer[j];
                    buffer[j] = aux;
                }
            }
        }
        //rastorn in arraylist vectorul sortat
        returndef.addAll(Arrays.asList(buffer).subList(0, len));
        if(returndef != null)
        {
            for (Definition i : returndef)
            {
                System.out.println(i.year);
            }
        }

        return returndef;
    }
}
