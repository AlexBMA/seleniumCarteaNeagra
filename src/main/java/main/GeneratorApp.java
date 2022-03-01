package main;

import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextRun;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static main.SeleniumResurseCrestine.closeSelenium;
import static main.SeleniumResurseCrestine.initSelenium;

public class GeneratorApp extends JFrame {
    JPanel fontColorDisplay, outlineColorDisplay, glowColorDisplay, backgroundColorDisplay;
    JSpinner fontSizeSpinner, outlineWidthSpinner, glowRadiusSpinner;
    JTextArea versesTextArea;
    JCheckBox lowerThirdCheckBox;
    JTextField inputFileTextField, outputFileTextField, linkInputField;
    List<String> verseList;

    final String FONTNAME = "Calibri";
    final FontGroup FONTGROUP = FontGroup.LATIN;
    final double FONT_SIZE = 60, OUTLINE_WIDTH = 1.5, GLOW_RADIUS = 2;
    final Color FONT_COLOR = Color.white, OUTLINE_COLOR = Color.black, GLOW_COLOR = Color.black, BACKGROUND_COLOR = new Color(5, 45 ,38);

    public GeneratorApp() {
        init();
    }

    private void init() {
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        setTitle("PowerPoint Generator App");
        setBounds(400, 200, 600, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pane.add(initResurseCrestineOptions());
        pane.add(initFileOptions());
        pane.add(initTextArea());
        pane.add(initSlideOptions());
        pane.add(initTextOptions());
        pane.add(initGenerateButton());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeSelenium();
            }
        });
    }

    private JPanel initResurseCrestineOptions(){
        JPanel linkContainer = new JPanel();
        Border textContainerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 5) ,"File options");
        linkContainer.setBorder(textContainerBorder);
        linkContainer.setLayout(new BoxLayout(linkContainer, BoxLayout.PAGE_AXIS));

        linkContainer.add(initInputWebSite());

        return linkContainer;
    }

    private JPanel initFileOptions() {
        JPanel fileContainer = new JPanel();
        Border textContainerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 5) ,"File options");
        fileContainer.setBorder(textContainerBorder);
        fileContainer.setLayout(new BoxLayout(fileContainer, BoxLayout.PAGE_AXIS));

        fileContainer.add(initInputFile());
        fileContainer.add(initOutputFile());

        return fileContainer;
    }

    private JPanel initInputWebSite(){
        JPanel inputFileContainer = new JPanel();
        inputFileContainer.setLayout(new FlowLayout());

        JLabel inputFileLabel = new JLabel("Input from resurse crestine:");
        linkInputField = new JTextField();
        linkInputField.setPreferredSize(new Dimension(300, 20));
        JButton linkButton = new JButton("Get Text");
        linkButton.addActionListener(e->{
            String text = linkInputField.getText();
            String textFromSite = SeleniumResurseCrestine.getSongTextFromResurseCrestine(text);
            versesTextArea.setText(textFromSite);
        });

        inputFileContainer.add(inputFileLabel);
        inputFileContainer.add(linkInputField);
        inputFileContainer.add(linkButton);

        return inputFileContainer;
    }

    private JPanel initInputFile() {
        JPanel inputFileContainer = new JPanel();
        inputFileContainer.setLayout(new FlowLayout());

        JLabel inputFileLabel = new JLabel("Input file:");
        inputFileTextField = new JTextField();
        inputFileTextField.setPreferredSize(new Dimension(300, 20));
        JButton inputFileButton = new JButton("Choose input file");
        inputFileButton.addActionListener(e -> readFromFile());

        inputFileContainer.add(inputFileLabel);
        inputFileContainer.add(inputFileTextField);
        inputFileContainer.add(inputFileButton);

        return inputFileContainer;
    }

    private JPanel initOutputFile() {
        JPanel outputFileContainer = new JPanel();
        outputFileContainer.setLayout(new FlowLayout());

        JLabel outputFileLabel = new JLabel("Output file:");
        outputFileTextField = new JTextField();
        outputFileTextField.setPreferredSize(new Dimension(300, 20));
        JButton outputFileButton = new JButton("Choose output file");
        outputFileButton.addActionListener(e -> {
            JFileChooser outputFileChooser = new JFileChooser();
            outputFileChooser.setFileFilter(new FileNameExtensionFilter("Ppt document (.pptx, .ppt)", "ppt", "pptx"));
            int result = outputFileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = outputFileChooser.getSelectedFile();
                outputFileTextField.setText(selectedFile.toString());
            }

        });

        outputFileContainer.add(outputFileLabel);
        outputFileContainer.add(outputFileTextField);
        outputFileContainer.add(outputFileButton);

        return outputFileContainer;
    }

    private JPanel initTextArea() {
        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.PAGE_AXIS));
        versesTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(versesTextArea);
        scrollPane.setPreferredSize(new Dimension(550, 450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        textAreaPanel.add(new JLabel("Verses"));
        textAreaPanel.add(scrollPane);

        return textAreaPanel;
    }

    private JPanel initSlideOptions() {
        JPanel slideContainer = new JPanel();
        Border slideContainerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 5) ,"Slide options");
        slideContainer.setBorder(slideContainerBorder);

        lowerThirdCheckBox = new JCheckBox("Use the subtitle format");
        JButton backgroundColorButton = new JButton("Choose background color");
        backgroundColorButton.addActionListener(e -> {
            Color backgroundColor = JColorChooser.showDialog(this, "Choose Background Color", BACKGROUND_COLOR);
            backgroundColorDisplay.setBackground(backgroundColor);
        });
        JLabel backgroundColorLabel = new JLabel("Background color:");
        backgroundColorDisplay = new JPanel();
        backgroundColorDisplay.setPreferredSize(new Dimension(25, 25));
        backgroundColorDisplay.setBackground(BACKGROUND_COLOR);

        slideContainer.setLayout(new FlowLayout());
        slideContainer.add(lowerThirdCheckBox);
        slideContainer.add(backgroundColorLabel);
        slideContainer.add(backgroundColorDisplay);
        slideContainer.add(backgroundColorButton);

        return slideContainer;
    }

    private JPanel initTextOptions() {
        JPanel textContainer = new JPanel();
        Border textContainerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 5) ,"Text options");
        textContainer.setBorder(textContainerBorder);
        textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.PAGE_AXIS));

        textContainer.add(initFontPanel());
        textContainer.add(initOutlinePanel());
        textContainer.add(initGlowPanel());

        return textContainer;
    }

    private JPanel initFontPanel() {
        JPanel fontPanel = new JPanel();

        JLabel fontSizeLabel = new JLabel("Text size (pt):");
        SpinnerNumberModel model = new SpinnerNumberModel(FONT_SIZE, 0.5, 150, 0.1);
        fontSizeSpinner = new JSpinner(model);

        JButton fontColorButton = new JButton("Choose text color");
        fontColorButton.addActionListener(e -> {
            Color fontColor = JColorChooser.showDialog(this, "Choose Text Color", FONT_COLOR);
            fontColorDisplay.setBackground(fontColor);
        });
        JLabel fontColorLabel = new JLabel("Text color:");
        fontColorDisplay = new JPanel();
        fontColorDisplay.setBackground(FONT_COLOR);
        fontColorDisplay.setPreferredSize(new Dimension(25, 25));

        fontPanel.setLayout(new FlowLayout());
        fontPanel.add(fontSizeLabel);
        fontPanel.add(fontSizeSpinner);
        fontPanel.add(fontColorLabel);
        fontPanel.add(fontColorDisplay);
        fontPanel.add(fontColorButton);

        return fontPanel;
    }

    private JPanel initOutlinePanel() {
        JPanel outlinePanel = new JPanel();

        JLabel outlineWidthLabel = new JLabel("Outline width (pt):");
        SpinnerNumberModel model = new SpinnerNumberModel(OUTLINE_WIDTH, 0, 50, 0.01);
        outlineWidthSpinner = new JSpinner(model);
        outlineWidthSpinner.setPreferredSize(new Dimension(50, 20));

        JButton outlineColorButton = new JButton("Choose outline color");
        outlineColorButton.addActionListener(e -> {
            Color outlineColor = JColorChooser.showDialog(this, "Choose Outline Color", OUTLINE_COLOR);
            outlineColorDisplay.setBackground(outlineColor);
        });
        JLabel outlineColorLabel = new JLabel("Outline color:");
        outlineColorDisplay = new JPanel();
        outlineColorDisplay.setBackground(OUTLINE_COLOR);
        outlineColorDisplay.setPreferredSize(new Dimension(25, 25));

        outlinePanel.setLayout(new FlowLayout());
        outlinePanel.add(outlineWidthLabel);
        outlinePanel.add(outlineWidthSpinner);
        outlinePanel.add(outlineColorLabel);
        outlinePanel.add(outlineColorDisplay);
        outlinePanel.add(outlineColorButton);

        return outlinePanel;
    }

    private JPanel initGlowPanel() {
        JPanel glowPanel = new JPanel();

        JLabel glowRadiusLabel = new JLabel("Glow radius (pt):");
        SpinnerNumberModel model = new SpinnerNumberModel(GLOW_RADIUS, 0, 50, 0.01);
        glowRadiusSpinner = new JSpinner(model);
        glowRadiusSpinner.setPreferredSize(new Dimension(50, 20));

        JButton glowColorButton = new JButton("Choose glow color");
        glowColorButton.addActionListener(e -> {
            Color glowColor = JColorChooser.showDialog(this, "Choose Glow Color", GLOW_COLOR);
            glowColorDisplay.setBackground(glowColor);
        });
        JLabel glowColorLabel = new JLabel("Glow color:");
        glowColorDisplay = new JPanel();
        glowColorDisplay.setBackground(GLOW_COLOR);
        glowColorDisplay.setPreferredSize(new Dimension(25, 25));

        glowPanel.setLayout(new FlowLayout());
        glowPanel.add(glowRadiusLabel);
        glowPanel.add(glowRadiusSpinner);
        glowPanel.add(glowColorLabel);
        glowPanel.add(glowColorDisplay);
        glowPanel.add(glowColorButton);

        return glowPanel;
    }

    private JPanel initGenerateButton() {
        JButton generatePptButton = new JButton("Generate ppt!");
        generatePptButton.addActionListener(e -> generatePpt());

        JPanel generatePanel = new JPanel();
        generatePanel.setLayout(new FlowLayout());
        generatePanel.add(generatePptButton);

        return generatePanel;
    }

    private void readFromFile() {
        JFileChooser inputFileChooser = new JFileChooser();
        int result = inputFileChooser.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION)
            return;

        File selectedFile = inputFileChooser.getSelectedFile();

        if (!selectedFile.exists()) {
            JOptionPane.showMessageDialog(null, "The input file doesn't exist!");
            return;
        }

        if (selectedFile.isDirectory()) {
            JOptionPane.showMessageDialog(null, "The input path should be of a file!");
            return;
        }

        inputFileTextField.setText(selectedFile.toString());

        try {
            String fileContent = Files.readString(Paths.get(selectedFile.toString()));
            versesTextArea.setText(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generatePpt() {
        String verseText = versesTextArea.getText().replace("\r", "");
        if(verseText.length() == 0) {
            JOptionPane.showMessageDialog(null, "You should provide some text to create a presentation!");
            return;
        }

        verseList = SlideGenerator.splitTextByVerses(verseText);
        boolean lowerThird = lowerThirdCheckBox.isSelected();

        if (lowerThird)
            verseList = SlideGenerator.createSubtitles(verseList);

        SlideGenerator.createPpt();

        Color slideBackgroundColor = backgroundColorDisplay.getBackground();
        SlideGenerator.setBackground(slideBackgroundColor.getRed(), slideBackgroundColor.getGreen(), slideBackgroundColor.getBlue());

        Color fontColor = fontColorDisplay.getBackground();
        Color outlineColor = outlineColorDisplay.getBackground();
        Color glowColor = glowColorDisplay.getBackground();
        double fontSize = (double) fontSizeSpinner.getValue();
        double outlineWidth = (double) outlineWidthSpinner.getValue();
        double glowRadius = (double) glowRadiusSpinner.getValue();

        if (outputFileTextField.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "The output file path is empty!");
            return;
        }

        File outputFilePath = new File(outputFileTextField.getText());

        if (!new File(outputFilePath.getParent()).exists()) {
            JOptionPane.showMessageDialog(null, "The containing directory doesn't exist!");
            return;
        }

        if (outputFilePath.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Please enter a file path!");
            return;
        }

        for(String verse : verseList) {
            XSLFSlide slide = SlideGenerator.addSlide();
            XSLFTextRun textRun = SlideGenerator.addText(slide, verse, fontColor, fontSize, FONTNAME, FONTGROUP, lowerThird);
            SlideGenerator.addGlow(textRun, glowRadius, glowColor.getRed(),glowColor.getGreen(),glowColor.getBlue());
            SlideGenerator.addOutline(textRun, outlineWidth, outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue());
        }
        SlideGenerator.addSlide();

        try {
            SlideGenerator.savePpt(outputFilePath.toString());

            Desktop desktop = Desktop.getDesktop();
            desktop.open(outputFilePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // JOptionPane.showMessageDialog(null, "Done!");
    }

    public static void main(String[] args) {
        initSelenium();
        EventQueue.invokeLater(() -> {
            var genApp = new GeneratorApp();
            genApp.setVisible(true);
        });
    }
}
