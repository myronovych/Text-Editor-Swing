/**
 * @author - Myronovych Oleksandr
 * NaUKMA , 1 year, computer science
 * Text Editor
 */


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;


public class TextEditor extends JFrame{

    private static final Color backgroundFunctions = new Color(124, 42, 230);
    private JTextPane textPane = new JTextPane();
    private static final Font defaultFont = new Font("Times New Roman", Font.PLAIN, 18);
    private JScrollPane scrollPane;
    private JPanel p = new JPanel();
    private File file__;
    private JFrame frame__;
    private UndoManager undoMgr__ ;
    private static final String[] FONT_LIST = {"Font ", "Arial", "Calibri", "Cambria", "Courier New", "Comic Sans MS", "Dialog", "Georgia", "Helevetica", "Lucida Sans", "Monospaced", "Tahoma", "Verdana"};
    private static final String[] FONT_SIZES = {"Size", "24", "26", "28", "30", "32", "34", "36", "38", "40"};
    private JComboBox<String> fontFamilyComboBox;
    private JComboBox<String> fontSizeComboBox;
    enum UndoActionType {UNDO, REDO};



    private TextEditor(){
        this.setFrameTitleWithExtn("Myronovych Word");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((dim.width-700)/2-(this.getSize().width)/2, (dim.height-700)/2-this.getSize().height/2);

        JPanel actions = new JPanel(new GridLayout(2,8));
        JPanel upperSection = new JPanel(new GridLayout(1,2));
        actions.setBackground(backgroundFunctions);
        textPane.setFont(defaultFont);
        scrollPane = new JScrollPane(textPane);
        p.setLayout(new BorderLayout());
        p.add(scrollPane,BorderLayout.CENTER);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open...");
        JMenu infoMenu = new JMenu("Info");
        JMenuItem save = new JMenuItem("Save");
        save.setMnemonic(KeyEvent.VK_S);
        save.addActionListener(new SaveFileListener());



        JMenuItem print = new JMenuItem("Print");
        print.addActionListener(new PrintFileListener());
        JMenuItem newItem = new JMenuItem("New");
        newItem.setMnemonic(KeyEvent.VK_N);
        newItem.addActionListener(new NewFileListener());

        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenuItem devInfo = new JMenuItem("Developer");

        devInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "This editor was designed and made by Myronovych Oleksandr.");
            }
        });


        menuBar.add(fileMenu);
        fileMenu.add(newItem);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(print);
        fileMenu.add(close);
        menuBar.add(infoMenu);
        infoMenu.add(devInfo);



        add(menuBar, BorderLayout.NORTH);
        add(actions,BorderLayout.SOUTH);

        open.addActionListener(new OpenFileListener());



        JButton bold = new JButton(new StyledEditorKit.BoldAction());
        ImageIcon boldIcon = new ImageIcon("src/res/bold.png");
        Image img = boldIcon.getImage() ;
        Image newimg = img.getScaledInstance( 10, 10,  java.awt.Image.SCALE_SMOOTH ) ;
        boldIcon = new ImageIcon( newimg );

        bold.setIcon(boldIcon);
        bold.setText("");
        actions.add(bold);

        JButton italic = new JButton(new StyledEditorKit.ItalicAction());
        ImageIcon italicIcon = new ImageIcon("src/res/italic.png");
         img = italicIcon.getImage() ;
         newimg = img.getScaledInstance( 10, 10,  java.awt.Image.SCALE_SMOOTH ) ;
        italicIcon = new ImageIcon( newimg );

        italic.setIcon(italicIcon);
        italic.setText("");
        actions.add(italic);

        JButton underline = new JButton(new StyledEditorKit.UnderlineAction());
        ImageIcon underlineIcon = new ImageIcon("src/res/underline.png");
        img = underlineIcon.getImage() ;
        newimg = img.getScaledInstance( 10, 10,  java.awt.Image.SCALE_SMOOTH ) ;
        underlineIcon = new ImageIcon( newimg );

        underline.setIcon(underlineIcon);
        underline.setText("");
        actions.add(underline);

        JButton color = new JButton("");
        ImageIcon colorIcon = new ImageIcon("src/res/palette.png");
        img = colorIcon.getImage() ;
        newimg = img.getScaledInstance( 15, 15,  java.awt.Image.SCALE_SMOOTH ) ;
        colorIcon = new ImageIcon( newimg );

        color.setIcon(colorIcon);
        color.addActionListener(new TextEditor.ColorActionListener());
        actions.add(color);

        JButton cut = new JButton(new DefaultEditorKit.CutAction());
        ImageIcon cutIcon = new ImageIcon("src/res/cut.png");
        img = cutIcon.getImage() ;
        newimg = img.getScaledInstance( 10, 10,  java.awt.Image.SCALE_SMOOTH ) ;
        cutIcon = new ImageIcon( newimg );

       cut.setIcon(cutIcon);
        cut.setText("");
        actions.add(cut);

        JButton copy = new JButton(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        actions.add(copy);

        JButton paste = new JButton(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        actions.add(paste);

        JButton breakParagraph = new JButton(new DefaultEditorKit.InsertBreakAction());
        breakParagraph.setText("Paragraph break");
        actions.add(breakParagraph);

        JButton tab = new JButton(new DefaultEditorKit.InsertTabAction());
        tab.setText("Tab break");
        actions.add(tab);


        JButton centerAll = new JButton(new StyledEditorKit.AlignmentAction(textPane.getSelectedText(), 1));
        ImageIcon centerIcon = new ImageIcon("src/res/center-alignment.png");
        img = centerIcon.getImage() ;
        newimg = img.getScaledInstance( 15, 15,  java.awt.Image.SCALE_SMOOTH ) ;
        centerIcon = new ImageIcon( newimg );

        centerAll.setIcon(centerIcon);
        centerAll.setText("");
        actions.add(centerAll);


        JButton leftAll = new JButton(new StyledEditorKit.AlignmentAction(textPane.getSelectedText(), 0));
        ImageIcon leftIcon = new ImageIcon("src/res/left-indent.png");
        img = leftIcon.getImage() ;
        newimg = img.getScaledInstance( 15, 15,  java.awt.Image.SCALE_SMOOTH ) ;
        leftIcon = new ImageIcon( newimg );

        leftAll.setIcon(leftIcon);
        leftAll.setText("");
        actions.add(leftAll);

        JButton rightAll = new JButton(new StyledEditorKit.AlignmentAction(textPane.getSelectedText(), 2));
        ImageIcon rightIcon = new ImageIcon("src/res/right-indent.png");
        img = rightIcon.getImage() ;
        newimg = img.getScaledInstance( 15, 15,  java.awt.Image.SCALE_SMOOTH ) ;
        rightIcon = new ImageIcon( newimg );

        rightAll.setIcon(rightIcon);
        rightAll.setText("");
        actions.add(rightAll);

        undoMgr__ = new UndoManager();

        textPane.setDocument(getNewDocument());


        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(new UndoActionListener(UndoActionType.UNDO));
        actions.add(undoButton);
        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(new UndoActionListener(UndoActionType.REDO));
        actions.add(redoButton);
        EditButtonActionListener editButtonActionListener =
                new EditButtonActionListener();




        fontFamilyComboBox = new JComboBox<String>(FONT_LIST);
        fontFamilyComboBox.setEditable(false);
        fontFamilyComboBox.addItemListener(new FontFamilyItemListener());
        actions.add(fontFamilyComboBox);


        fontSizeComboBox = new JComboBox<String>(FONT_SIZES);
        fontSizeComboBox.setEditable(false);
        fontSizeComboBox.addItemListener(new FontSizeItemListener());
        actions.add(fontSizeComboBox);





        add(p);
        setPreferredSize(new Dimension(700,700));
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private StyledDocument getNewDocument() {

        StyledDocument doc = new DefaultStyledDocument();
        doc.addUndoableEditListener(new UndoEditListener());
        return doc;
    }

    private class OpenFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            file__ = chooseFile();

            if (file__ == null) {

                return;
            }

            readFile(file__);
            setFrameTitleWithExtn(file__.getName());
        }

        private File chooseFile() {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showOpenDialog(frame__) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            }
            else {
                return null;
            }
        }

        private void readFile(File file) {

            StyledDocument doc = null;

            try (InputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)) {

                doc = (DefaultStyledDocument) ois.readObject();
            }
            catch (FileNotFoundException ex) {

                JOptionPane.showMessageDialog(frame__, "Input file was not found!");
                return;
            }
            catch (ClassNotFoundException | IOException ex) {

                throw new RuntimeException(ex);
            }

            textPane.setDocument(doc);
            doc.addUndoableEditListener(new UndoEditListener());
            applyFocusListenerToPictures(doc);
        }

        private void applyFocusListenerToPictures(StyledDocument doc) {

            ElementIterator iterator = new ElementIterator(doc);
            Element element;

            while ((element = iterator.next()) != null) {

                AttributeSet attrs = element.getAttributes();


            }
        }
    }

    private void setFrameTitleWithExtn(String titleExtn) {

        this.setTitle("" + titleExtn);
    }

    private class UndoEditListener implements UndoableEditListener {

        @Override
        public void undoableEditHappened(UndoableEditEvent e) {

            undoMgr__.addEdit(e.getEdit()); // remember the edit
        }
    }

    private class SaveFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (file__ == null) {

                file__ = chooseFile();

                if (file__ == null) {

                    return;
                }
            }

            DefaultStyledDocument doc = (DefaultStyledDocument) getEditorDocument();

            try (OutputStream fos = new FileOutputStream(file__);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                oos.writeObject(doc);
            }
            catch (IOException ex) {

                throw new RuntimeException(ex);
            }

            setFrameTitleWithExtn(file__.getName());
        }

        private File chooseFile() {

            JFileChooser chooser = new JFileChooser();

            if (chooser.showSaveDialog(frame__) == JFileChooser.APPROVE_OPTION) {

                return chooser.getSelectedFile();
            }
            else {
                return null;
            }
        }
    }

    //внутрішній клас для вибору кольору тексту
    private class ColorActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Color newColor =
                    JColorChooser.showDialog(null, "Choose a color", Color.BLACK);
            if (newColor == null) {
                textPane.requestFocusInWindow();
                return;
            }
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setForeground(attr, newColor);
            textPane.setCharacterAttributes(attr, false);
            textPane.requestFocusInWindow();
        }
    }

    private class UndoActionListener implements ActionListener {

        private UndoActionType undoActionType;

        public UndoActionListener(UndoActionType type) {

            undoActionType = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            switch (undoActionType) {

                case UNDO:
                    if (! undoMgr__.canUndo()) {

                        textPane.requestFocusInWindow();
                        return; // no edits to undo
                    }

                    undoMgr__.undo();
                    break;

                case REDO:
                    if (! undoMgr__.canRedo()) {

                        textPane.requestFocusInWindow();
                        return; // no edits to redo
                    }

                    undoMgr__.redo();
            }

            textPane.requestFocusInWindow();
        }
    }


    private class PrintFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                textPane.setContentType("text/html");
                boolean done = textPane.print();
                if (done) {
                    JOptionPane.showMessageDialog(null, "Printing is done");
                }
            } catch (Exception pex) {
                JOptionPane.showMessageDialog(null, "Error");
                pex.printStackTrace();
            }
        }
    }

    private class NewFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            initEditorAttributes();
            textPane.setDocument(getNewDocument());
            file__ = null;
            setFrameTitleWithExtn("New file");
        }

        private void initEditorAttributes() {

            AttributeSet attrs1 = textPane.getCharacterAttributes();
            SimpleAttributeSet attrs2 = new SimpleAttributeSet(attrs1);
            attrs2.removeAttributes(attrs1);
            textPane.setCharacterAttributes(attrs2, true);
        }
    }

    private class FontFamilyItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() != ItemEvent.SELECTED) ||
                    (fontFamilyComboBox.getSelectedIndex() == 0)) {
                return;
            }
            String fontFamily = (String) e.getItem();
            fontFamilyComboBox.setAction(new StyledEditorKit.FontFamilyAction(fontFamily, fontFamily));
            fontFamilyComboBox.setSelectedIndex(0);
        }
    }

    private class FontSizeItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() != ItemEvent.SELECTED) ||
                    (fontSizeComboBox.getSelectedIndex() == 0)) {
                return;
            }

            String fontSizeStr = (String) e.getItem();
            int newFontSize = 0;

            try {
                newFontSize = Integer.parseInt(fontSizeStr);
            } catch (NumberFormatException ex) {

                return;
            }

            fontSizeComboBox.setAction(new StyledEditorKit.FontSizeAction(fontSizeStr, newFontSize));
            fontSizeComboBox.setSelectedIndex(0);

        }
    }

    private StyledDocument getEditorDocument() {

        StyledDocument doc = (DefaultStyledDocument) textPane.getDocument();
        return doc;
    }



    public static void main(String[] args) {
        new TextEditor();
    }

    private class EditButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            textPane.requestFocusInWindow();
        }
    }



}

