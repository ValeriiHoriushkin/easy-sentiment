package sentiwordnet3;//    Copyright 2013 Petter Törnberg
//
//    This demo code has been kindly provided by Petter Törnberg <pettert@chalmers.se>
//    for the SentiWordNet website.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static strings.StringWorker.*;


public class SentiWordNet {

    private Map<String, Double> dictionary;

    private SentiWordNet(String pathToSWN) throws IOException {
        dictionary = new HashMap<String, Double>();
        HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();
        BufferedReader csv = null;
        try {
            csv = new BufferedReader(new FileReader(pathToSWN));
            int lineNumber = 0;
            String line;
            while ((line = csv.readLine()) != null) {
                lineNumber++;
                if (!line.trim().startsWith("#")) {
                    String[] data = line.split("\t");
                    String wordTypeMarker = data[0];
                    if (data.length != 6) {
                        throw new IllegalArgumentException(
                                "Incorrect tabulation format in file, line: "
                                        + lineNumber);
                    }
                    Double synsetScore = Double.parseDouble(data[2])-Double.parseDouble(data[3]);
                    String[] synTermsSplit = data[4].split(" ");
                    for (String synTermSplit : synTermsSplit) {
                        String[] synTermAndRank = synTermSplit.split("#");
                        String synTerm = synTermAndRank[0] + "#" + wordTypeMarker;
                        int synTermRank = Integer.parseInt(synTermAndRank[1]);
                        if (!tempDictionary.containsKey(synTerm)) {
                            tempDictionary.put(synTerm,
                                    new HashMap<Integer, Double>());
                        }
                        tempDictionary.get(synTerm).put(synTermRank,
                                synsetScore);
                    }
                }
            }
            for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary.entrySet()) {
                String word = entry.getKey();
                Map<Integer, Double> synSetScoreMap = entry.getValue();
                double score = 0.0;
                double sum = 0.0;
                for (Map.Entry<Integer, Double> setScore : synSetScoreMap
                        .entrySet()) {
                    score += setScore.getValue() / (double) setScore.getKey();
                    sum += 1.0 / (double) setScore.getKey();
                }
                score /= sum;
                dictionary.put(word, score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csv != null) {
                csv.close();
            }
        }
    }

    private double extract(String word, String pos) {
        return dictionary.get(word + "#" + pos);
    }

    //Aaaaaa bbbb ccccc ddddd .... zzzzz.(!,?)
    private double getSentimentScoreByVocabulary(String text){
        splitToSentence("Today  ^^  very good    day. Yeah, it is great!!!!!!!! I agree with you. ...  ");
        String[] wordsInSentence = text.split(" ");
        String lastWord = wordsInSentence[wordsInSentence.length-1];
        String typeSentence = lastWord.substring(lastWord.length()-1);
        wordsInSentence[wordsInSentence.length-1] = lastWord.substring(0,lastWord.length()-1);
        for (String s : wordsInSentence){
            //System.out.println(s);
        }
        return 0.0;
    }


    public static void main(String [] args) throws IOException {
        if(args.length<1) {
            System.err.println("Usage: java sentiwordnet3.SentiWordNet <pathToSentiWordNetFile>");
            return;
        }

        String pathToSWN = args[0];
        SentiWordNet sentiwordnet = new SentiWordNet(pathToSWN);
        sentiwordnet.getSentimentScoreByVocabulary("Today very good day. Yeah, it is great!!!! I agree with you.");


        /*System.out.println("today "+sentiwordnet.extract("today", ""));
        System.out.println("bad#a "+sentiwordnet.extract("bad", "a"));
        System.out.println("blue#a "+sentiwordnet.extract("blue", "a"));
        System.out.println("blue#n "+sentiwordnet.extract("blue", "n"));*/
    }
}