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
 * ����� ��������� ������� ����� � ��������. ��������, ������ "�����" ����� ������������� � "Tekst".
 * @author A.Tsvetkov 2010 http://tsvetkov.at.ua mailto:al@ukr.net
 *
 */
public class Translit {

        private static final String[]   charTable       = new String[88];

        private static final char               START_CHAR      = '�';
        private static final char               CODER_CHAR      = '#';

        static {
                charTable['�' - START_CHAR] = "A";
                charTable['�' - START_CHAR] = "B";
                charTable['�' - START_CHAR] = "V";
                charTable['�' - START_CHAR] = "G";
                charTable['�' - START_CHAR] = "D";
                charTable['�' - START_CHAR] = "E";
                charTable['�' - START_CHAR] = "YO";
                charTable['�' - START_CHAR] = "ZH";
                charTable['�' - START_CHAR] = "Z";
                charTable['�' - START_CHAR] = "I";
                charTable['�' - START_CHAR] = "YI";
                charTable['�' - START_CHAR] = "K";
                charTable['�' - START_CHAR] = "L";
                charTable['�' - START_CHAR] = "M";
                charTable['�' - START_CHAR] = "N";
                charTable['�' - START_CHAR] = "O";
                charTable['�' - START_CHAR] = "P";
                charTable['�' - START_CHAR] = "R";
                charTable['�' - START_CHAR] = "S";
                charTable['�' - START_CHAR] = "T";
                charTable['�' - START_CHAR] = "U";
                charTable['�' - START_CHAR] = "F";
                charTable['�' - START_CHAR] = "H";
                charTable['�' - START_CHAR] = "C";
                charTable['�' - START_CHAR] = "CH";
                charTable['�' - START_CHAR] = "SH";
                charTable['�' - START_CHAR] = "SCH";
                charTable['�' - START_CHAR] = "''";
                charTable['�' - START_CHAR] = "Y";
                charTable['�' - START_CHAR] = "'";
                charTable['�' - START_CHAR] = "YE";
                charTable['�' - START_CHAR] = "YU";
                charTable['�' - START_CHAR] = "YA";
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
         * ��������� ������� ����� � ��������. � �������������� ������ ������ ������� ����� ����� �������� �� ��������������� ����������. �� ������� ������� ��������� ��������.
         * @param text �������� ����� � �������� ���������
         * @return ���������
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
         * ��������� ������� ����� � ��������. 
         * � �������������� ������ ������ ������� ����� ����� �������� �� ��������������� ���������� + ���������� ����. 
         * �� ������� ������� ��������� ��������.
         * @param text �������� ����� � �������� ���������
         * @return ���������
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
