package com.wangrj.java_lib.test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 创建 Entity 示例列表用于测试
 */
public class EntityExampleCreator {

    private int start = 1;
    private int count = 1;
    private boolean containsSuperClassFields = false;// 是否需要给父类的属性赋值
    private Set<String> existsParameterizedClass = new HashSet<>();

    public EntityExampleCreator start(int start) {
        this.start = start;
        return this;
    }

    public EntityExampleCreator count(int count) {
        this.count = count;
        return this;
    }

    public EntityExampleCreator containsSuperClassFields(boolean containsSuperClassFields) {
        this.containsSuperClassFields = containsSuperClassFields;
        return this;
    }

    private EntityExampleCreator existsParameterizedClass(Set<String> existsParameterizedClass) {
        this.existsParameterizedClass = existsParameterizedClass;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> create(Class<T> entityClass) throws Exception {
        List<T> entityList = new ArrayList<>();
        Field[] declaredFields = entityClass.getDeclaredFields();
        Field[] superClassFields = entityClass.getSuperclass().getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            fields.add(declaredField);
        }
        if (containsSuperClassFields) {
            fields.addAll(Arrays.asList(superClassFields));
        }
        for (int i = start; i < start + count; i++) {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if ((modifiers & Modifier.STATIC) != 0 || (modifiers & Modifier.FINAL) != 0) {// 如果是static或者final修饰，就跳过不赋值
                    continue;
                }
                field.setAccessible(true);
                if (field.getType().isEnum()) {// 如果类型是Enum，特殊处理
                    Class type = field.getType();
                    Object[] enumConstants = type.getEnumConstants();
                    if (enumConstants.length > 0) {
                        field.set(entity, enumConstants[(i - start) % enumConstants.length]);
                    }
                    continue;
                }
                switch (field.getType().getSimpleName()) {
                    case "int":
                    case "Integer":
                        field.set(entity, i);
                        break;
                    case "long":
                    case "Long":
                        field.set(entity, (long) i);
                        break;
                    case "float":
                    case "Float":
                        field.set(entity, (float) i);
                        break;
                    case "double":
                    case "Double":
                        field.set(entity, (double) i);
                        break;
                    case "boolean":
                    case "Boolean":
                        field.set(entity, true);
                        break;
                    case "String":
                        field.set(entity, field.getName() + "_" + i);
                        break;
                    case "Date":
                        field.set(entity, new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24) * (i - start)));
                        break;
                    case "LocalDate":
                        field.set(entity, LocalDate.now().plusDays(i - start));
                        break;
                    case "LocalDateTime":
                        field.set(entity, LocalDateTime.now().plusDays(i - start));
                        break;
                    case "List":
                    case "Set":
                        Class parameterizedClass = null;
                        ParameterizedType type = (ParameterizedType) field.getGenericType();
                        Type[] types = type.getActualTypeArguments();
                        if (types != null && types.length > 0) {
                            parameterizedClass = (Class) types[0];
                        }
                        if (parameterizedClass != null) {
                            if (!existsParameterizedClass.contains(parameterizedClass.getName())) {// 预防循环引用导致无限递归
                                existsParameterizedClass.add(parameterizedClass.getName());
                                List list = create(parameterizedClass);
                                if (field.getType().getSimpleName().equals("List")) {
                                    field.set(entity, list);
                                } else if (field.getType().getSimpleName().equals("Set")) {
                                    field.set(entity, new HashSet<>(list));
                                }
                            }
                        }
                    default:
                        if (!existsParameterizedClass.contains(field.getType().getName())) {// 预防循环引用导致无限递归
                            existsParameterizedClass.add(field.getType().getName());
                            try {
                                List list = new EntityExampleCreator().
                                        start(start).
                                        count(count).
                                        containsSuperClassFields(containsSuperClassFields).
                                        existsParameterizedClass(new HashSet<>(existsParameterizedClass)).
                                        create(field.getType());
                                field.set(entity, list.get(0));
                                break;
                            } catch (Exception ignored) {
                            }
                        }
                }
            }
            entityList.add(entity);
        }
        return entityList;
    }

}
