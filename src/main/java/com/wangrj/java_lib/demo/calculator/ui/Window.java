package com.wangrj.java_lib.demo.calculator.ui;

import com.wangrj.java_lib.demo.calculator.constant.C;
import com.wangrj.java_lib.demo.calculator.datastruct.Expression;
import com.wangrj.java_lib.demo.calculator.datastruct.SignsManager;
import com.wangrj.java_lib.demo.calculator.util.Compiler;
import com.wangrj.java_lib.demo.calculator.util.MyFile;
import com.wangrj.java_lib.demo.calculator.util.Util;

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
        new Window("计算器（自定义运算符）");
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

        itemWelcome = new JMenuItem("欢迎使用");
        itemWelcome.addActionListener(this);
        menuCal = new JMenu("计算器");
        menuCal.add(itemWelcome);

        itemUseHelp = new JMenuItem("使用说明");
        itemUseHelp.addActionListener(this);
        itemAbout = new JMenuItem("关于作者");
        itemAbout.addActionListener(this);
        menuHelp = new JMenu("帮助");

        menuHelp.add(itemUseHelp);
        menuHelp.add(itemAbout);

        menuBar = new JMenuBar();
        menuBar.add(menuCal);
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

    }

    private void initCalculateArea() {

        rbtnPre = new JRadioButton("前缀");
        rbtnPre.addActionListener(this);
        rbtnIn = new JRadioButton("中缀");
        rbtnIn.addActionListener(this);
        bgPreOrIn = new ButtonGroup();
        bgPreOrIn.add(rbtnPre);
        bgPreOrIn.add(rbtnIn);
        rbtnPre.setSelected(true);
        rbtnIn.setSelected(false);

        checkBoxHasParameter = new JCheckBox("是否含参数");
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

        btnClear = new JButton("清空");
        btnClear.addActionListener(this);
        btnCalculate = new JButton("计算");
        btnCalculate.addActionListener(this);
        btnMergeConst = new JButton("合并常数项");
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
        baseBox.add(new JLabel("前缀表达式，以空格分开"));
        baseBox.add(textPreExpr);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(new JLabel("中缀表达式"));
        baseBox.add(textInExpr);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(new JLabel("参数列表，用空格分开"));
        baseBox.add(new JLabel("如a=1 b=2 c=3"));
        baseBox.add(textParameter);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(boxHorBtn);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(new JLabel("后缀表达式"));
        baseBox.add(textPostExpr);
        baseBox.add(Box.createVerticalStrut(20));

        baseBox.add(new JLabel("结果"));
        baseBox.add(textResult);

        JPanel panel = new JPanel();
        panel.add(baseBox);
        add(panel, BorderLayout.WEST);
    }

    private void initSignArea() {

        btnUpdate = new JButton("修改该运算符");
        btnUpdate.addActionListener(this);
        btnDelete = new JButton("删除该运算符");
        btnDelete.addActionListener(this);
        btnCreate = new JButton("新建运算符>>");
        btnCreate.addActionListener(this);

        comboBox = new JComboBox<>(new String[]{});
        UpdateUI.updateJComboBox(SignsManager.getSigns(), comboBox);

        JPanel panel = new JPanel();
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(40));
        JTextField textField = new JTextField("符号 名称\t优先级 目");
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
        box.add(new JLabel("符号:"));
        box.add(Box.createHorizontalStrut(10));
        box.add(textSign);
        box.add(Box.createHorizontalStrut(20));
        box.add(new JLabel("名称:"));
        box.add(Box.createHorizontalStrut(20));
        box.add(textName);
        baseBox.add(box);

        box = Box.createHorizontalBox();
        bgOperateNum = new ButtonGroup();
        rbtnDouble = new JRadioButton("双目");
        rbtnSigle = new JRadioButton("单目");
        rbtnDouble.setSelected(true);
        bgOperateNum.add(rbtnSigle);
        bgOperateNum.add(rbtnDouble);
        btnSave = new JButton("修改");
        btnSave.addActionListener(this);
        box.add(rbtnDouble);
        box.add(rbtnSigle);
        box.add(Box.createHorizontalStrut(20));
        box.add(new JLabel("优先级(1-20):"));
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
            MyJOptionPane.showInfoPane("使用说明：\n" + MyFile.readHelp());

            // 前缀
        } else if (e.getSource() == rbtnPre) {
            textPreExpr.setEditable(true);
            textInExpr.setEditable(false);

            // 中缀
        } else if (e.getSource() == rbtnIn) {
            textPreExpr.setEditable(false);
            textInExpr.setEditable(true);

            // 是否含参数
        } else if (e.getSource() == checkBoxHasParameter) {
            if (checkBoxHasParameter.isSelected()) {
                textParameter.setEditable(true);
            } else {
                textParameter.setEditable(false);
            }

            // 计算
        } else if (e.getSource() == btnCalculate) {

            try {

                boolean isPre = rbtnPre.isSelected();
                String strExpr = isPre ? textPreExpr.getText() : textInExpr
                        .getText();
                if (strExpr.length() > 0) {

                    // 前缀(中缀)表达式构造表达式类型对象
                    expr = new Expression(strExpr, isPre);

                    textPreExpr.setText(expr.writePreExpr() + "");
                    textPostExpr.setText(expr.writePostExpr() + "");

                    if (checkBoxHasParameter.isSelected()) {// 表达式含参
                        Util.assignParameterFromText(expr.exprTree,
                                textParameter.getText());
                    }

                    textResult.setText(expr.value() + "");
                }
            } catch (Exception e1) {
                MyJOptionPane.showErrorPane(C.MSG_ERROR);
                e1.printStackTrace();
            }

            // 清除
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

            // 合并常数项
        } else if (e.getSource() == btnMergeConst) {
            if (expr != null) {
                expr.mergeConst();
                textPreExpr.setText(expr.writePreExpr() + "");
                textPostExpr.setText(expr.writePostExpr() + "");
            }

            // 修改
        } else if (e.getSource() == btnUpdate) {
            if (SignsManager.isEmpty()) {
                return;
            }
            textSign.setEditable(false);
            btnSave.setText("修改");
            UpdateUI.updateSignUI(textSign, textName, textPrior, textCode,
                    rbtnDouble, rbtnSigle,
                    SignsManager.getSignByStrSign(getSelectedSign()));

            // 删除
        } else if (e.getSource() == btnDelete) {
            if (SignsManager.isEmpty()) {
                return;
            }
            SignsManager.delete(getSelectedSign());
            UpdateUI.updateJComboBox(SignsManager.getSigns(), comboBox);

            // 新建
        } else if (e.getSource() == btnCreate) {
            textSign.setEditable(true);
            btnSave.setText("新建");
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
            if (btnSave.getText().equals("新建")) {

                if (!SignsManager.exist(sign)) {
                    SignsManager.add(sign, name, operateNum, prior, midCode);
                    UpdateUI.updateJComboBox(SignsManager.getSigns(), comboBox);
                    MyJOptionPane.showInfoPane(C.TIP_NEW_SUCCESS);
                } else {
                    MyJOptionPane.showErrorPane(C.TIP_SIGN_EXISTED_ERROR);
                }

            } else if (btnSave.getText().equals("修改")) {
                SignsManager.update(sign, name, operateNum, prior, midCode);
                UpdateUI.updateJComboBox(SignsManager.getSigns(), comboBox);
                MyJOptionPane.showInfoPane(C.TIP_UPDATE_SUCCESS);
            }

        }
    }
}