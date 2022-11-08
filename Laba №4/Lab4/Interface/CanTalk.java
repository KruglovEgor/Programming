package Lab4.Interface;

import Lab4.Exceptions.ForeignLanguageException;

//TODO - check ask or add the Exception to the say


public interface CanTalk {
    default String say(String txt) throws ForeignLanguageException{
        String rus_txt = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя  -.,!?:;+=-$*()";
        for (int i = 0; i < txt.length(); i++){
            int flag = 0;
            for (int j = 0; j < rus_txt.length(); j++){
                if (txt.charAt(i) == rus_txt.charAt(j)){
                    flag = 1;
                    break;
                }
            }
            if (flag == 0){
                throw new ForeignLanguageException("Используйте пожалйста русские буквы!");
            }
        }
        return "сказал: " + '"' + txt + '"';
    }


    default String ask(String txt) throws ForeignLanguageException{
        String rus_txt = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя -.,!?:;+=-$*()";
        for (int i = 0; i < txt.length(); i++){
            int flag = 0;
            for (int j = 0; j < rus_txt.length(); j++){
                if (txt.charAt(i) == rus_txt.charAt(j)){
                    flag = 1;
                    break;
                }
            }
            if (flag == 0){
                throw new ForeignLanguageException("Используйте пожалйста русские буквы!");
            }
        }
        return "спросил " + txt;
    }
}
