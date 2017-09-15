package com.wangrg.java_program.carton_title_parser;

import com.wangrg.java_util.ConfigUtil;
import com.wangrg.java_util.FileUtil;
import com.wangrg.java_util.JOptionPaneUtil;
import com.wangrg.java_util.TextUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

/**
 * by ���ٿ� on 2016/7/26.
 */
public class ParserWindow extends JFrame implements ActionListener {

    private static final String TITLE = "�������������������ͨ���ٶȰٿƣ�";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private static final int MODE_URL = 0;
    private static final int MODE_PATH = 1;
    private int mode;

    JLabel labelUrl = new JLabel("�ٶȰٿ���ַ��");
    JComboBox cbMode = new JComboBox<>(new Object[]{"�ٶȰٿ�", "html�ļ�"});
    JTextField tfUrl = new JTextField();
    JComboBox cbColumn = new JComboBox<>(new Object[]{"��1��", "��2��", "��3��", "��4��", "��5��"});
    JButton btnParse1 = new JButton("����1");
    JButton btnParse2 = new JButton("����2");
    JButton btnParseDelete = new JButton("ɾ���ո��������");
    final JTextArea taResult = new JTextArea();

    JMenuItem openFileMenu = new JMenuItem("��html�ļ�");
    JMenuItem exitMenu = new JMenuItem("�˳�");
    JMenuItem useMenu = new JMenuItem("ʹ�÷���");
    JMenuItem aboutMenu = new JMenuItem("����");

    static {
        ConfigUtil.read(Config.class, "parserConfig.txt", true);
    }

    public ParserWindow() {
        super(TITLE);

        initMenu();
        initTopPanel();
        initCenterPanel();
        setMode(MODE_URL);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    private void setMode(int mode) {
        this.mode = mode;
        if (mode == MODE_URL) {
            labelUrl.setText("�ٶȰٿ���ַ��");
        } else if (mode == MODE_PATH) {
            labelUrl.setText("html�ļ�·����");
        }
    }

    public void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("�ļ�");
        JMenu menu2 = new JMenu("����");

        menu1.add(openFileMenu);
        menu1.addSeparator();
        menu1.add(exitMenu);
        menu2.add(useMenu);
        menu2.addSeparator();
        menu2.add(aboutMenu);
        openFileMenu.addActionListener(this);
        exitMenu.addActionListener(this);
        useMenu.addActionListener(this);
        aboutMenu.addActionListener(this);

        menuBar.add(menu1);
        menuBar.add(menu2);
        setJMenuBar(menuBar);
    }

    public void initCenterPanel() {
        JScrollPane scrollPane = new JScrollPane(taResult);
        scrollPane.setBounds(100, 90, 450, 300);
        add(scrollPane, "Center");
    }

    public void initTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);

        labelUrl.setBounds(0, 0, 100, 30);
        tfUrl.setBounds(100, 0, 600, 30);
        cbMode.setBounds(0, 40, 90, 30);
        cbColumn.setBounds(100, 40, 90, 30);
        btnParse1.setBounds(200, 40, 80, 30);
        btnParse2.setBounds(290, 40, 80, 30);
        btnParseDelete.setBounds(500, 40, 200, 30);

        topPanel.add(labelUrl);
        topPanel.add(tfUrl);
        topPanel.add(cbMode);
        topPanel.add(cbColumn);
        topPanel.add(btnParse1);
        topPanel.add(btnParse2);
        topPanel.add(btnParseDelete);

        tfUrl.addActionListener(this);
        btnParse1.addActionListener(this);
        btnParse2.addActionListener(this);
        btnParseDelete.addActionListener(this);
        cbMode.addActionListener(this);

        JPanel tmpPanel = new JPanel();
        tmpPanel.add(topPanel, "Center");

        topPanel.setPreferredSize(new Dimension(700, 90));
        add(tmpPanel, "North");

    }

    /**
     * 1:�����ٶȰٿƵĸ�������
     * 2:�����ٶȰٿƵķּ�����
     */
    private int parseMode = 1;

    public void actionPerformed(ActionEvent paramActionEvent) {
        Object source = paramActionEvent.getSource();
        if (source == openFileMenu) {
            openHtmlFile();

        } else if (source == cbMode) {
            setMode(cbMode.getSelectedIndex());

        } else if (source == btnParse1) {
            parseMode = 1;
            if (mode == MODE_PATH) {
                parseHtmlFile(false, 1);

            } else if (mode == MODE_URL) {
                parseUrl(false, 1);
            }

        } else if (source == btnParse2) {
            parseMode = 2;
            if (mode == MODE_PATH) {
                parseHtmlFile(false, 2);

            } else if (mode == MODE_URL) {
                parseUrl(false, 2);
            }

        } else if (source == btnParseDelete) {
            if (mode == MODE_PATH) {
                parseHtmlFile(true, parseMode);

            } else if (mode == MODE_URL) {
                parseUrl(true, parseMode);
            }

        } else if (source == exitMenu) {
            System.exit(0);

        }
    }

    private void openHtmlFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("ѡ��html�ļ�");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File(Config.defaultDir));
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".html");
            }

            @Override
            public String getDescription() {
                return null;
            }
        });

        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File htmlFile = fileChooser.getSelectedFile();
            Config.defaultDir = TextUtil.getTextExceptLastSlash(htmlFile.getAbsolutePath());
            tfUrl.setText(htmlFile.getAbsolutePath());
            setMode(MODE_PATH);
        }
    }

    private BaiKeVideoTitleParser.TitleHandler handler = new BaiKeVideoTitleParser.TitleHandler() {
        @Override
        public String updateTitle(String title) {
            String[] split = title.split("[ ]");
            if (split.length > 0) {
                return split[split.length - 1];
            } else {
                return title;
            }
        }
    };

    private void parseUrl(boolean deleteTextBeforeSpace, int parseMode) {
        String url = tfUrl.getText();
        if (!TextUtil.isEmpty(url)) {
            try {
                Document document = Jsoup.connect(url).get();
                String result = "";
                if (parseMode == 1) {
                    result = BaiKeVideoTitleParser.parse1(document, cbColumn.getSelectedIndex(),
                            deleteTextBeforeSpace ? handler : null);
                } else if (parseMode == 2) {
                    result = BaiKeVideoTitleParser.parse2(document,
                            deleteTextBeforeSpace ? handler : null);
                }
                taResult.setText(result);
            } catch (Exception e) {
                JOptionPaneUtil.showError(e.toString());
            }
        }
    }

    private void parseHtmlFile(boolean deleteTextBeforeSpace, int parseMode) {
        String htmlFilePath = tfUrl.getText();
        if (TextUtil.isEmpty(htmlFilePath)) {
            return;
        }
        String html = FileUtil.read(htmlFilePath);
        try {
            Document document = Jsoup.parse(html);
            String result = "";
            if (parseMode == 1) {
                result = BaiKeVideoTitleParser.parse1(document, cbColumn.getSelectedIndex(),
                        deleteTextBeforeSpace ? handler : null);
            } else if (parseMode == 2) {
                result = BaiKeVideoTitleParser.parse2(document,
                        deleteTextBeforeSpace ? handler : null);
            }
            taResult.setText(result);
        } catch (Exception e) {
            JOptionPaneUtil.showError(e.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        new ParserWindow();
    }

}
