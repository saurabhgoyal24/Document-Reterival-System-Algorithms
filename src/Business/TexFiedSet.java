/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

import UserInterface.MainJFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Saurabh Goyal
 */
class TexFiedSet implements ActionListener {

    private JTextArea txtArea;
    private File file;
    private HighlightPainter painter1;
//    private String[] queryTerms;

    public TexFiedSet(JTextArea textArea, File file) {
        txtArea = textArea;
        this.file = file;
        painter1 = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        try {

            String content = FileUtils.readFileToString(file);
//            String content1 = content.replaceAll("[\\\t|\\\n|\\\r]", " ");
            txtArea.setText(content);
            MainJFrame.highlight(txtArea);
        } catch (IOException ex) {
            Logger.getLogger(TexFiedSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private void highlight(JTextArea textArea, String[] pattern) {
//
//        try {
//            Highlighter highlighter = textArea.getHighlighter();
//            Document doc = textArea.getDocument();
//            String text = doc.getText(0, doc.getLength());
//
//            for (String word : pattern) {
//                int pos = 0;
//                while ((pos = text.indexOf(word, pos)) >= 0) {
//                    highlighter.addHighlight(pos, pos + word.length(), painter1);
//                    pos += word.length();
//                }
//            }
//        } catch (BadLocationException e) {
//            System.out.print(e);
//        }
//    }
}
