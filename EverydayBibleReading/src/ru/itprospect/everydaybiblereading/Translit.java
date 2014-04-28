package ru.itprospect.everydaybiblereading;

/*******************************************************************************
 * Copyright (c) 2012 Alexandr Tsvetkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Alexandr Tsvetkov - initial API and implementation
 * 
 * Project:
 *     TAO Library
 * 
 * File name:
 *     Translit.java
 *     
 * License agreement:
 *
 * 1. This code is published AS IS. Author is not responsible for any damage that can be
 *    caused by any application that uses this code.
 * 2. Author does not give a garantee, that this code is error free.
 * 3. This code can be used in NON-COMMERCIAL applications AS IS without any special
 *    permission from author.
 * 4. This code can be modified without any special permission from author IF AND ONLY IF
 *    this license agreement will remain unchanged.
 * 5. SPECIAL PERMISSION for this code usage in COMMERCIAL application SHOULD be obtained
 *    from author.
 ******************************************************************************/

/**
 * 
 * Класс переводит русский текст в транслит. Например, строка "Текст" будет преобразована в "Tekst".
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 *
 */
public class Translit {

        private static final String[]   charTable       = new String[88];

        private static final char               START_CHAR      = 'Ё';
        private static final char               CODER_CHAR      = '#';

        static {
                charTable['А' - START_CHAR] = "A";
                charTable['Б' - START_CHAR] = "B";
                charTable['В' - START_CHAR] = "V";
                charTable['Г' - START_CHAR] = "G";
                charTable['Д' - START_CHAR] = "D";
                charTable['Е' - START_CHAR] = "E";
                charTable['Ё' - START_CHAR] = "YO";
                charTable['Ж' - START_CHAR] = "ZH";
                charTable['З' - START_CHAR] = "Z";
                charTable['И' - START_CHAR] = "I";
                charTable['Й' - START_CHAR] = "YI";
                charTable['К' - START_CHAR] = "K";
                charTable['Л' - START_CHAR] = "L";
                charTable['М' - START_CHAR] = "M";
                charTable['Н' - START_CHAR] = "N";
                charTable['О' - START_CHAR] = "O";
                charTable['П' - START_CHAR] = "P";
                charTable['Р' - START_CHAR] = "R";
                charTable['С' - START_CHAR] = "S";
                charTable['Т' - START_CHAR] = "T";
                charTable['У' - START_CHAR] = "U";
                charTable['Ф' - START_CHAR] = "F";
                charTable['Х' - START_CHAR] = "H";
                charTable['Ц' - START_CHAR] = "C";
                charTable['Ч' - START_CHAR] = "CH";
                charTable['Ш' - START_CHAR] = "SH";
                charTable['Щ' - START_CHAR] = "SCH";
                charTable['Ъ' - START_CHAR] = "''";
                charTable['Ы' - START_CHAR] = "Y";
                charTable['Ь' - START_CHAR] = "'";
                charTable['Э' - START_CHAR] = "YE";
                charTable['Ю' - START_CHAR] = "YU";
                charTable['Я' - START_CHAR] = "YA";
                //charTable[' ' - START_CHAR] = "_";

                for (int i = 0; i < charTable.length; i++) {
                        char idx = (char) ((char) i + START_CHAR);
                        char lower = new String(new char[] { idx }).toLowerCase().charAt(0);
                        if (charTable[i] != null) {
                                charTable[lower - START_CHAR] = charTable[i].toLowerCase();
                        }
                }
        }

        /**
         * Переводит русский текст в транслит. В результирующей строке каждая русская буква будет заменена на соответствующую английскую. Не русские символы останутся прежними.
         * @param text исходный текст с русскими символами
         * @return результат
         */
        public static String toTranslit(String text) {
                char charBuffer[] = text.toCharArray();
                StringBuilder sb = new StringBuilder(text.length());
                for (char symbol : charBuffer) {
                        int i = symbol - START_CHAR;
                        if (i >= 0 && i < charTable.length) {
                                String replace = charTable[i];
                                sb.append(replace == null ? symbol : replace);
                        } else {
                                sb.append(symbol);
                        }
                }
                return sb.toString();
        }

        /**
         * Переводит русский текст в транслит. 
         * В результирующей строке каждая русская буква будет заменена на соответствующую английскую + кодирующий знак. 
         * Не русские символы останутся прежними.
         * @param text исходный текст с русскими символами
         * @return результат
         */
        public static String toTraslitCoded(String text) {
                char charBuffer[] = text.toCharArray();
                StringBuilder sb = new StringBuilder(text.length());
                for (char symbol : charBuffer) {
                        sb.append(CODER_CHAR);
                        int i = symbol - START_CHAR;
                        if (i >= 0 && i < charTable.length) {
                                String replace = charTable[i];
                                sb.append(replace == null ? symbol : replace);
                        } else {
                                sb.append(symbol);
                        }
                }
                return sb.toString();
        }
}
