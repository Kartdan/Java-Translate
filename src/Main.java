import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        //dictionarul de cuvinte
        ArrayList<Combined> dictionary = new ArrayList<>();

        //directorul din care citesc fisierele json
        File dir = new File("/Users/Dani Cartale/Desktop/Facultate/Anul 2/Sem 1/POO/Tema_2/src/dictionaries");
        File[] directories = dir.listFiles();

        if (directories != null)
        {
            //parcurg fisierele din director
            for (File child : directories)
            {
                try {
                    //language
                    String file_name = child.getName();
                    String[] parts = file_name.split("_");

                    //parcurg fisierul json
                    JsonElement fileElement = JsonParser.parseReader(new FileReader(child));
                    JsonArray wordArray = fileElement.getAsJsonArray();
                    for(JsonElement contor : wordArray)
                    {
                        Combined combined_buffer = new Combined();

                        //instanta a clasei Word
                        Word word_buffer = new Word();

                        JsonObject obj = contor.getAsJsonObject();
                        //word
                        String word = obj.get("word").getAsString();
                        //word_en
                        String word_en = obj.get("word_en").getAsString();
                        //type
                        String type = obj.get("type").getAsString();

                        //singular
                        JsonArray singular_arr = obj.get("singular").getAsJsonArray();
                        ArrayList<String> singular = new ArrayList<>();
                        int len = singular_arr.size();
                        for(int i = 0; i < len; i++)
                        {
                            singular.add(singular_arr.get(i).toString());
                        }

                        //plural
                        JsonArray plural_arr = obj.get("plural").getAsJsonArray();
                        ArrayList<String> plural = new ArrayList<>();
                        len = plural_arr.size();
                        for(int i = 0; i < len; i++)
                        {
                            plural.add(plural_arr.get(i).toString());
                        }

                        //imi completez campurile instantei clasei Word
                        word_buffer.setWord(word);
                        word_buffer.setWord_en(word_en);
                        word_buffer.setType(type);
                        word_buffer.singular = singular;
                        word_buffer.plural = plural;

                        //definition
                        //Definition definition_buffer = new Definition();
                        ArrayList<Definition> definition_buffer_list = new ArrayList<>();
                        JsonArray definitions_arr = obj.get("definitions").getAsJsonArray();
                        for(JsonElement def : definitions_arr)
                        {
                            JsonObject def_object = def.getAsJsonObject();
                            //instanta a clasei Definition
                            Definition definition_buffer = new Definition();

                            //dict
                            String dict = def_object.get("dict").getAsString();
                            //dictType
                            String dictType = def_object.get("dictType").getAsString();
                            //year
                            int year = def_object.get("year").getAsInt();

                            //text
                            JsonArray text_arr = def_object.get("text").getAsJsonArray();
                            ArrayList<String> text = new ArrayList<>();
                            len = text_arr.size();
                            for(int i = 0; i < len; i++)
                            {
                                text.add(text_arr.get(i).toString());
                            }

                            //imi completez campurile instantei clasei Definition
                            definition_buffer.setDict(dict);
                            definition_buffer.setDictType(dictType);
                            definition_buffer.setYear(year);
                            definition_buffer.text = text;

                            definition_buffer_list.add(definition_buffer);

                        }

                        //completez campurile instantei Combined, formand un cuvant complet,
                        //pe care il adaug in dictionar
                        combined_buffer.definition = definition_buffer_list;
                        combined_buffer.language = parts[0];
                        combined_buffer.word = word_buffer;

                        dictionary.add(combined_buffer);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        //Metodele
        Methods methods = new Methods(dictionary);

        /////////////////////////method add word/////////////////////////
        Word word_1 = new Word();
        word_1.setWord("caine");
        methods.addWord(word_1, "ro");
        word_1.setWord("varza");
        methods.addWord(word_1, "ro");


        ////////////////////////method remove word////////////////////////
        methods.removeWord("caine", "ro");
        methods.removeWord("varza", "ro");
        methods.removeWord("miere", "ro");

        ///////////////////////method add definition//////////////////////
        Definition add_def = new Definition();
        add_def.year = 2000;
        add_def.dict = "Dicționar de sinonime";
        add_def.dictType = "synonyms";
        add_def.text.add("a se potrivi");
        methods.addDefinitionForWord("merge", "ro", add_def);
        add_def.dictType = "definitions";
        methods.addDefinitionForWord("merge", "ro", add_def);

        //////////////////////method remove definition//////////////////
        methods.removeDefinition("merge", "ro", "Dicționarul explicativ al limbii române (ediția a II-a revăzută și adăugită)");
        methods.removeDefinition("merge", "ro", "Dicționar de sinonime");

        /////////////////////method translate word///////////////////////
        String word = "merge";
        String fromLanguage = "ro";
        String toLanguage = "fr";
        System.out.println(methods.translateWord(word, fromLanguage, toLanguage));

        ///////////////////////method translate sentence///////////////
        String translate = "pisică mănâncă";
        fromLanguage = "ro";
        toLanguage = "fr";
        System.out.println(methods.translateSentence(translate, fromLanguage, toLanguage));
        translate = "merge pisică";
        System.out.println(methods.translateSentence(translate, fromLanguage, toLanguage));

        //////////////////////method get definitions for word/////////////////////
        methods.getDefinitionsForWord("câine", "ro");
        methods.getDefinitionsForWord("varză", "ro");




    }
}
