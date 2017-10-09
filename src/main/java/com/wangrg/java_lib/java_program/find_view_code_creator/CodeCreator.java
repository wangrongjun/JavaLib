package com.wangrg.java_lib.java_program.find_view_code_creator;

import com.wangrg.java_lib.data_structure.PairList;
import com.wangrg.java_lib.java_util.TextUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * by wangrongjun on 2017/3/1.
 */
public class CodeCreator {

    public static String createDefineAndFindViewCode(String xmlText, boolean returnDefine) {
        PairList<String, String> idViewList = parseXml(xmlText);
//        DebugUtil.printlnEntity(idViewList);
        String defineViewCode = "";
        String findViewByIdCode = "";
        for (int i = 0; i < idViewList.size(); i++) {
            String id = idViewList.getLeft(i);
            String view = idViewList.getRight(i);
            view = TextUtil.getTextAfterLastPoint(view);//把类名前面可能存在的包名去掉
            String fieldName = TextUtil.toUpperCaseStyle(id);
            defineViewCode += "private " + view + " " + fieldName + ";\n";
            findViewByIdCode += fieldName + " = (" + view + ")findViewById(R.id." + id + ");\n";
        }
        return returnDefine ? defineViewCode : findViewByIdCode;
    }

    public static String createViewHolderCode(String xmlText) {
        String defineCode = createDefineAndFindViewCode(xmlText, true);
        defineCode = defineCode.replace("private ", "");
        String findViewCode = createDefineAndFindViewCode(xmlText, false);
        findViewCode = findViewCode.replace("findViewById", "view.findViewById");

        String result = "static class ViewHolder{\n\n";
        result += defineCode + "\n";
        result += "ViewHolder(View view){\n";
        result += findViewCode + "}\n}";
        return result;
    }

    public static String createOnClickCode(String xmlText, boolean onlyBtn) {
        String setListenerCode = "";
        String onClickCode = "";
        PairList<String, String> idViewList = parseXml(xmlText);
        for (int i = 0; i < idViewList.size(); i++) {
            String id = idViewList.getLeft(i);
            if (!id.startsWith("btn") && onlyBtn) {//如果只需要btn型，就把所有非btn开头的id排除
                continue;
            }
            setListenerCode += TextUtil.toUpperCaseStyle(id) + ".setOnClickListener(this);\n";
            if (!TextUtil.isEmpty(onClickCode)) {
                onClickCode += "} else ";
            }
            onClickCode += "if(view.getId() == R.id." + id + ") {\n\n";
        }
        if (!TextUtil.isEmpty(onClickCode)) {
            onClickCode += "}";
        }
        onClickCode = "@Override\npublic void onClick(View view){\n" + onClickCode + "\n}";

        return setListenerCode + "\n" + onClickCode;
    }

    private static PairList<String, String> parseXml(String xmlText) {
        PairList<String, String> idViewList = new PairList<>();

        String[] lines = xmlText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            //需要匹配：android:id="@+id/btn_video_downloader"
            Pattern pattern = Pattern.compile("id=\"@\\+id/([^\"]+)\"");
            Matcher matcher = pattern.matcher(lines[i]);
            if (matcher.find()) {//当前行有id属性
                String id = matcher.group(1);
                //找到这个id对应的View的名称，一般在上一行或上两行
                String viewName = "";
                int j = i - 1;
                while (j >= 0) {
                    String beforeLine = lines[j];
                    if (beforeLine.contains("<")) {
                        if (beforeLine.contains("xmlns")) {//如果LinearLayout后跟着xmlns命名空间
                            Pattern p = Pattern.compile("<([^ ]+)");//匹配空格前的<LinearLayout
                            Matcher m = p.matcher(beforeLine);
                            if (m.find()) {
                                viewName = m.group(1);
                            } else {//理论上不可能来到这里
                                viewName = "Error: see CodeCreator-parseXml()";
                            }
                        } else {//否则如beforeLine=“  <LinearLayout”那样，正常处理
                            viewName = beforeLine.
                                    replace("<", "").
                                    replace("\n", "").
                                    replace("\r", "").
                                    replace(" ", "");
                        }
                        break;
                    }
                }
                idViewList.add(id, viewName);
            }
        }

        return idViewList;
    }

}
