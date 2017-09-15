package com.wangrg.java_program.find_view_code_creator;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class FindViewCodeCreatorWindow extends JFrame implements ActionListener {

    private static final String TITLE = "FindViewCodeCreator";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    private JSplitPane splitPane;
    private JTextArea taLeft = new JTextArea();
    private JTextArea taRight = new JTextArea();
    private JButton btnCreateDefineCode = new JButton("Create(Define)");
    private JButton btnCreateFindViewCode = new JButton("Create(FindView)");
    private JButton btnCreateOnClickCode = new JButton("Create(OnClick)");
    private JButton btnCreateViewHolder = new JButton("Create(ViewHolder)");

    public FindViewCodeCreatorWindow() {
        super(TITLE);
        initView();
        initEvent();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        splitPane.setDividerLocation(0.5);
    }

    private void initView() {
        JPanel topButtonsPanel = new JPanel();
        topButtonsPanel.add(btnCreateDefineCode);
        topButtonsPanel.add(btnCreateFindViewCode);
        topButtonsPanel.add(btnCreateOnClickCode);
        topButtonsPanel.add(btnCreateViewHolder);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(taLeft), new JScrollPane(taRight));
        add(topButtonsPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }

    private void initEvent() {
        btnCreateDefineCode.addActionListener(this);
        btnCreateFindViewCode.addActionListener(this);
        btnCreateOnClickCode.addActionListener(this);
        btnCreateViewHolder.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String xmlText = taLeft.getText();

        if (e.getSource() == btnCreateDefineCode) {
            String code = CodeCreator.createDefineAndFindViewCode(xmlText, true);
            taRight.setText(code);

        } else if (e.getSource() == btnCreateFindViewCode) {
            String code = CodeCreator.createDefineAndFindViewCode(xmlText, false);
            taRight.setText(code);

        } else if (e.getSource() == btnCreateOnClickCode) {
            String code = CodeCreator.createOnClickCode(xmlText, false);
            taRight.setText(code);

        } else if (e.getSource() == btnCreateViewHolder) {
            String code = CodeCreator.createViewHolderCode(xmlText);
            taRight.setText(code);

        }
    }

    public static void main(String[] a) {
        new FindViewCodeCreatorWindow();
    }
}
