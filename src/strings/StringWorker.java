package strings;

import java.util.ArrayList;
import java.util.List;

public class StringWorker {

    public static String[] splitToSentence(String text){
        System.out.println(text+"\n");
        String[][] resultString;
        int amountOfSentences = 0;
        StringBuilder sb = new StringBuilder();
        boolean replacementMade = false;
        char[] textCharArr = text.toCharArray();
        for (int i=0; i<textCharArr.length;i++){
            if (i<textCharArr.length-1){
                if ( (textCharArr[i] == '.' || textCharArr[i] == '!' || textCharArr[i] == '?') && !replacementMade ){
                    sb.append("\n");
                    amountOfSentences++;
                    replacementMade = true;
                }
                else if (textCharArr[i] == ' ' && replacementMade) {
                    continue;
                }
                else if (!(textCharArr[i] == '.' || textCharArr[i] == '!' || textCharArr[i] == '?')){
                    //check ACCII - uppercase, lowercase, space
                    if (textCharArr[i]>=65 && textCharArr[i]<=90 || textCharArr[i]>=97 && textCharArr[i]<=122 || textCharArr[i] == 32) {
                        //System.out.println(textCharArr[i]+"="+(int)textCharArr[i]);
                        sb.append(textCharArr[i]);
                    }
                    replacementMade = false;
                }
            }
        }
        resultString = new String[amountOfSentences][];
        String[] temp = sb.toString().split("\n");
        for (int i=0; i<temp.length; i++){
            String[] temp2 = temp[i].split(" ");
            int[] notNullIndexes;
            int notNullIndexesSize = 0;
            for (String s: temp2){
                if (!s.equals("")){
                    notNullIndexesSize++;
                }
            }
            resultString[i] = new String[notNullIndexesSize];
            int k = 0;
            for (int j=0; j<temp2.length; j++){
                if (!temp2[j].equals("")){
                    resultString[i][k++] = temp2[j];
                }
            }
        }
        for (int i=0; i<resultString.length; i++){
            for (int j=0; j<resultString[i].length;j++){
                System.out.print("["+resultString[i][j]+"]  ");
            }
            System.out.println();
        }
        return null;
    }
}
