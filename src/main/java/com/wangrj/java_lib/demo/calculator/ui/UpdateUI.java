package com.wangrj.java_lib.demo.calculator.ui;

import com.wangrj.java_lib.demo.calculator.bean.Sign;
import com.wangrj.java_lib.demo.calculator.bean.Sign;

import java.awt.TextArea;
import java.awt.TextField;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;


public class UpdateUI {

    public static void updateJComboBox(List<Sign> signs,
                                       JComboBox<String> comboBox) {
        if (signs == null || comboBox == null) {
            return;
        }
        String[] s = new String[signs.size()];
        for (int i = 0; i < signs.size(); i++) {
            s[i] = "  " + signs.get(i).getSign() + "     "
                    + signs.get(i).getName() + "                  "
                    + +signs.get(i).getPrior() + "       "
                    + (signs.get(i).getOperateNum() == 1 ? "单目" : "双目");
        }
        comboBox.setModel(new DefaultComboBoxModel<String>(s));

    }

    public static void updateSignUI(TextField textSign, TextField textName,
                                    TextField textPrior, TextArea textCode, JRadioButton rbtnDouble,
                                    JRadioButton rbtnSigle, Sign sign) {
        textSign.setText(sign.getSign());
        textName.setText(sign.getName());
        textPrior.setText(sign.getPrior() + "");
        textCode.setText(sign.getCode());
        if (sign.getOperateNum() == 2) {
            rbtnDouble.setSelected(true);
        } else {
            rbtnSigle.setSelected(true);
        }

    }

    public static void clearSignUI(TextField textSign, TextField textName,
                                   TextField textPrior, TextArea textCode, JRadioButton rbtnDouble) {
        textSign.setText("");
        textName.setText("");
        textPrior.setText("");
        textCode.setText("");
        rbtnDouble.setSelected(true);

    }
}
