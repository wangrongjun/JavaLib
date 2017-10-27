package com.wangrj.java_lib.demo.calculator.ui;

import com.wangrj.java_lib.demo.calculator.constant.C;
import com.wangrj.java_lib.demo.calculator.datastruct.Expression;
import com.wangrj.java_lib.demo.calculator.datastruct.SignsManager;
import com.wangrj.java_lib.demo.calculator.util.Compiler;
import com.wangrj.java_lib.demo.calculator.util.MyFile;
import com.wangrj.java_lib.demo.calculator.util.Util;
import com.wangrj.java_lib.demo.calculator.util.Compiler;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class Window extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JMenuBar menuBar;
    private JMenu menuCal, menuHelp;
    private JMenuItem itemWelcome, itemAbout, itemUseHelp;

    private TextField textPreExpr, textInExpr, textParameter, textPostExpr,
            textResult, textSign, textPrior, textName;
    private TextArea textCode;

    private JButton btnCalculate, btnClear, btnMergeConst, btnSave, btnDelete,
            btnUpdate, btnCreate;

    private JCheckBox checkBoxHasParameter;

    private JComboBox<String> comboBox;

    private ButtonGroup bgOperateNum;
    private JRadioButton rbtnSigle, rbtnDouble;

    private ButtonGroup bgPreOrIn;
    private JRadioButton rbtnPre, rbtnIn;

    public static void main(String[] args) {
        new Window("���������Զ����������");
    }

    public Window(String title) {
        setTitle(title);
        setBounds(250, 200, 800, 450);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());
        initMenu();
        initCalculateArea();
        initSignArea();
        initCodeArea();
        initWindow();
        validate();
    }

    class MyWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            MyFile.saveSigns(SignsManager.getSigns());
            System.exit(0);
        }
    }

    private void initWindow() {
        addWindowListener(new MyWindowAdapter());
    }

    private void initMenu() {

        itemWelcome = new JMenuItem("��ӭʹ��");
        itemWelcome.addActionListener(this);
        menuCal = new JMenu("������");
        menuCal.add(itemWelcome);

        itemUseHelp = new JMenuItem("ʹ��˵��");
        itemUseHelp.addActionListener(this);
        itemAbout = new JMenuItem("��������");
        itemAbout.addActionListener(this);
        menuHelp = new JMenu("����");

        menuHelp.add(itemUseHelp);
        menuHelp.add(itemAbout);

        menuBar = new JMenuBar();
        menuBar.add(menuCal);
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

    }

    private void initCalculateArea() {

        rbtnPre = new JRadioButton("ǰ׺");
        rbtnPre.addActionListener(this);
        rbtnIn = new JRadioButton("��׺");
        rbtnIn.addActionListener(this);
        bgPreOrIn = new ButtonGroup();
        bgPreOrIn.add(rbtnPre);
        bgPreOrIn.add(rbtnIn);
        rbtnPre.setSelected(true);
        rbtnIn.setSelected(false);

        checkBoxHasParameter = new JCheckBox("�Ƿ񺬲���");
        checkBoxHasParameter.setSelected(false);
        checkBoxHasParameter.addActionListener(this);

        textPreExpr = new TextField(25);
        textPreExpr.setEditable(true);
        textInExpr = new TextField(25);
        textInExpr.setEditable(false);
        textParameter = new TextField(25);
        textParameter.setEditable(false);
        textPostExpr = new TextField(25);
        textPostExpr.setEditable(false);
        textResult = new TextField();
        textResult.setEditable(false);

        btnClear = new JButton("���");
        btnClear.addActionListener(this);
        btnCalculate = new JButton("����");
        btnCalculate.addActionListener(this);
        btnMergeConst = new JButton("�ϲ�������");
        btnMergeConst.addActionListener(this);

        Box boxHorRbtn = Box.createHorizontalBox();
        boxHorRbtn.add(rbtnPre);
        boxHorRbtn.add(Box.createHorizontalStrut(20));
        boxHorRbtn.add(rbtnIn);
        boxHorRbtn.add(Box.createHorizontalStrut(20));
        boxHorRbtn.add(checkBoxHasParameter);

        Box boxHorBtn = Box.createHorizontalBox();
        boxHorBtn.add(btnClear);
        boxHorBtn.add(Box.createHorizontalStrut(10));
        boxHorBtn.add(btnCalculate);
        // boxHorBtn.add(Box.createHorizontalStrut(10));
        // boxHorBtn.add(btnMergeConst);

        Box baseBox = Box.createVerticalBox();
        baseBox.add(boxHorRbtn);
        baseBox.add(Box.createVerticalStrut(20));
        baseBox.add(new JLabel("ǰ׺���ʽ���Կո�ֿ�"));
        baseBox.add(textPreExpr);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(new JLabel("��׺���ʽ"));
        baseBox.add(textInExpr);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(new JLabel("�����б��ÿո�ֿ�"));
        baseBox.add(new JLabel("��a=1 b=2 c=3"));
        baseBox.add(textParameter);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(boxHorBtn);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(new JLabel("��׺���ʽ"));
        baseBox.add(textPostExpr);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(new JLabel("���"));
        baseBox.add(textResult);

        JPanel panel = new JPanel();
        panel.add(baseBox);
        add(panel, BorderLayout.WEST);
    }

    private void initSignArea() {

        btnUpdate = new JButton("�޸ĸ������");
        btnUpdate.addActionListener(this);
        btnDelete = new JButton("ɾ���������");
        btnDelete.addActionListener(this);
        btnCreate = new JButton("�½������>>");
        btnCreate.addActionListener(this);

        comboBox = new JComboBox<>(new String[]{});
        UpdateUI.updateJComboBox(SignsManager.getSigns(), comboBox);

        JPanel panel = new JPanel();
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(40));
        JTextField textField = new JTextField("���� ����\t���ȼ� Ŀ");
        textField.setEditable(false);
        box.add(textField);
        box.add(comboBox);
        box.add(Box.createVerticalStrut(15));
        box.add(btnUpdate);
        box.add(Box.createVerticalStrut(15));
        box.add(btnDelete);
        box.add(Box.createVerticalStrut(15));
        box.add(btnCreate);
        panel.add(box);
        add(panel, BorderLayout.CENTER);

    }

    private void initCodeArea() {
        JTextField text1 = new JTextField(
                "double fun(double n1,double n2) {   ");
        JTextField text2 = new JTextField("}");
        text1.setEditable(false);
        text2.setEditable(false);
        Box baseBox = Box.createVerticalBox();
        baseBox.add(text1);
        textCode = new TextArea(10, 20);
        baseBox.add(textCode);
        baseBox.add(text2);

        Box box = Box.createHorizontalBox();

        textSign = new TextField(1);
        textName = new TextField(5);
        box.add(new JLabel("����:"));
        box.add(Box.createHorizontalStrut(10));
        box.add(textSign);
        box.add(Box.createHorizontalStrut(20));
        box.add(new JLabel("����:"));
        box.add(Box.createHorizontalStrut(20));
        box.add(textName);
        baseBox.add(box);

        box = Box.createHorizontalBox();
        bgOperateNum = new ButtonGroup();
        rbtnDouble = new JRadioButton("˫Ŀ");
        rbtnSigle = new JRadioButton("��Ŀ");
        rbtnDouble.setSelected(true);
        bgOperateNum.add(rbtnSigle);
        bgOperateNum.add(rbtnDouble);
        btnSave = new JButton("�޸�");
        btnSave.addActionListener(this);
        box.add(rbtnDouble);
        box.add(rbtnSigle);
        box.add(Box.createHorizontalStrut(20));
        box.add(new JLabel("���ȼ�(1-20):"));
        textPrior = new TextField(2);
        box.add(textPrior);
        box.add(btnSave);
        baseBox.add(box);

        JTextField textField = new JTextField(C.tip1_1);
        textField.setEditable(false);
        baseBox.add(textField);

        textField = new JTextField(C.tip1_2);
        textField.setEditable(false);
        baseBox.add(textField);

        textField = new JTextField(C.tip2_1);
        textField.setEditable(false);
        baseBox.add(textField);

        textField = new JTextField(C.tip2_2);
        textField.setEditable(false);
        baseBox.add(textField);

        add(baseBox, BorderLayout.EAST);

    }

    private Expression expr;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == itemWelcome) {
            MyJOptionPane.showInfoPane(C.welcomeWord);

        } else if (e.getSource() == itemAbout) {
            MyJOptionPane.showInfoPane(C.programmerInfo);

        } else if (e.getSource() == itemUseHelp) {
            MyJOptionPane.showInfoPane("ʹ��˵����\n" + MyFile.readHelp());

            // ǰ׺
        } else if (e.getSource() == rbtnPre) {
            textPreExpr.setEditable(true);
            textInExpr.setEditable(false);

            // ��׺
        } else if (e.getSource() == rbtnIn) {
            textPreExpr.setEditable(false);
            textInExpr.setEditable(true);

            // �Ƿ񺬲���
        } else if (e.getSource() == checkBoxHasParameter) {
            if (checkBoxHasParameter.isSelected()) {
                textParameter.setEditable(true);
            } else {
                textParameter.setEditable(false);
            }

            // ����
        } else if (e.getSource() == btnCalculate) {

            try {

                boolean isPre = rbtnPre.isSelected();
                String strExpr = isPre ? textPreExpr.getText() : textInExpr
                        .getText();
                if (strExpr.length() > 0) {

                    // ǰ׺(��׺)���ʽ������ʽ���Ͷ���
                    expr = new Expression(strExpr, isPre);

                    textPreExpr.setText(expr.writePreExpr() + "");
                    textPostExpr.setText(expr.writePostExpr() + "");

                    if (checkBoxHasParameter.isSelected()) {// ���ʽ����
                        Util.assignParameterFromText(expr.exprTree,
                                textParameter.getText());
                    }

                    textResult.setText(expr.value() + "");
                }
            } catch (Exception e1) {
                MyJOptionPane.showErrorPane(C.MSG_ERROR);
                e1.printStackTrace();
            }

            // ���
        } else if (e.getSource() == btnClear) {
            if (rbtnPre.isSelected()) {
                textPreExpr.setText(" ");
                textPreExpr.setText("");

            } else {
                textInExpr.setText(" ");
                textInExpr.setText("");
            }

            textParameter.setText(" ");
            textParameter.setText("");

            // �ϲ�������
        } else if (e.getSource() == btnMergeConst) {
            if (expr != null) {
                expr.mergeConst();
                textPreExpr.setText(expr.writePreExpr() + "");
                textPostExpr.setText(expr.writePostExpr() + "");
            }

            // �޸�
        } else if (e.getSource() == btnUpdate) {
            if (SignsManager.isEmpty()) {
                return;
            }
            textSign.setEditable(false);
            btnSave.setText("�޸�");
            UpdateUI.updateSignUI(textSign, textName, textPrior, textCode,
                    rbtnDouble, rbtnSigle,
                    SignsManager.getSignByStrSign(getSelectedSign()));

            // ɾ��
        } else if (e.getSource() == btnDelete) {
            if (SignsManager.isEmpty()) {
                return;
            }
            SignsManager.delete(getSelectedSign());
            UpdateUI.updateJComboBox(SignsManager.getSigns(), comboBox);

            // �½�
        } else if (e.getSource() == btnCreate) {
            textSign.setEditable(true);
            btnSave.setText("�½�");
            UpdateUI.clearSignUI(textSign, textName, textPrior, textCode,
                    rbtnDouble);

        } else if (e.getSource() == btnSave) {
            save();
        }
    }

    private String getSelectedSign() {
        return comboBox.getSelectedItem().toString().substring(2, 3);

    }

    private void save() {
        String name = textName.getText();
        String sign = textSign.getText();
        String midCode = textCode.getText();
        int operateNum = rbtnDouble.isSelected() ? 2 : 1;
        int prior = 0;
        try {
            prior = Integer.parseInt(textPrior.getText());
        } catch (NumberFormatException e) {
            MyJOptionPane.showErrorPane(C.TIP_ERROR);
            return;
        }

        if (name.length() < 1 || midCode.length() < 1) {
            MyJOptionPane.showErrorPane(C.TIP_ERROR);

        } else if (textSign.getText().length() != 1
                || sign.matches("[0123456789.()]")) {
            MyJOptionPane.showErrorPane(C.TIP_SIGN_ERROR);

        } else if (C.ERROR == Compiler.compile(C.preCode + textCode.getText()
                + C.postCode)) {
            MyJOptionPane.showErrorPane(C.TIP_COMPILE_ERROR);
        } else {
            if (btnSave.getText().equals("�½�")) {

                if (!SignsManager.exist(sign)) {
                    SignsManager.add(sign, name, operateNum, prior, midCode);
                    UpdateUI.updateJComboBox(SignsManager.getSigns(), comboBox);
                    MyJOptionPane.showInfoPane(C.TIP_NEW_SUCCESS);
                } else {
                    MyJOptionPane.showErrorPane(C.TIP_SIGN_EXISTED_ERROR);
                }

            } else if (btnSave.getText().equals("�޸�")) {
                SignsManager.update(sign, name, operateNum, prior, midCode);
                UpdateUI.updateJComboBox(SignsManager.getSigns(), comboBox);
                MyJOptionPane.showInfoPane(C.TIP_UPDATE_SUCCESS);
            }

        }
    }
}