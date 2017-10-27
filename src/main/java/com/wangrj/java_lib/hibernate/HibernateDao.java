package com.wangrj.java_lib.hibernate;

import com.wangrj.java_lib.java_util.ReflectUtil;
import com.wangrj.java_lib.java_util.ReflectUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.List;

/**
 * by wangrongjun on 2017/6/13.
 */
public class HibernateDao<T> implements Dao<T> {

    /**
     * session使用完是否马上关闭。该变量是为了使用OpenSessionInView时轻易改变关闭时期。
     */
    public static boolean openSessionInView = false;
    public static ThreadLocal<Session> threadLocal;

    private static SessionFactory sessionFactory;
    private Class<T> entityClass;
    private Session session;

    public static void buildSessionFactory(String hbm2ddlAuto) {
        Configuration configure = new Configuration().configure("hibernate.cfg.xml");
        if (hbm2ddlAuto != null) {
            configure.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        }
        sessionFactory = configure.buildSessionFactory();
    }

    public static void buildSessionFactory() {
        buildSessionFactory(null);
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    protected Session openSession() {
//        if (openSessionInView) {
//            return threadLocal.get();
//        } else {
            if (session == null) {
                session = getSessionFactory().openSession();
            }
            return session;
//        }
    }

    protected void closeSession() {
        if (session != null && !openSessionInView) {
            session.close();
            session = null;
        }
    }

    public HibernateDao() {
//        if (sessionFactory == null) {
//            buildSessionFactory();
//        }
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getEntityClass() {
        if (entityClass == null) {
            entityClass = ReflectUtil.getGenericClass(getClass());
        }
        return entityClass;
    }

    protected String getTableName() {
        Entity tableAnno = getEntityClass().getAnnotation(Entity.class);
        if (tableAnno != null) {
            String name = tableAnno.name();
            if (name.length() > 0) {
                return name;
            }
        }
        return getEntityClass().getSimpleName();
    }

    protected Class<?> getIdFieldType() {
        Field field = ReflectUtil.findByAnno(getEntityClass(), Id.class);
        if (field == null) {
            throw new RuntimeException(getEntityClass().getName() + " : id field not found");
        }
        return field.getType();
    }

    protected List<T> executeNativeQuery(String sql) {
        return executeNativeQuery(sql, 0, 0);
    }

    protected List<T> executeNativeQuery(String sql, int offset, int rowCount) {
        Session session = openSession();
        NativeQuery<T> query = session.createNativeQuery(sql, getEntityClass());
        if (offset >= 0 && rowCount > 0) {
            query.setFirstResult(offset);
            query.setMaxResults(rowCount);
        }
        List<T> list = query.list();
        closeSession();
        return list;
    }

    protected List<T> executeQuery(String hql) {
        return executeQuery(hql, 0, 0);
    }

    protected List<T> executeQuery(String hql, int offset, int rowCount) {
        Session session = openSession();
        Query<T> query = session.createQuery(hql, getEntityClass());
        if (offset >= 0 && rowCount > 0) {
            query.setFirstResult(offset);
            query.setMaxResults(rowCount);
        }
        List<T> list = query.list();
        closeSession();
        return list;
    }

    protected int executeQueryCount(String hql) {
        Session session = openSession();
        Long count = (Long) session.createQuery(hql).uniqueResult();
        closeSession();
        return count.intValue();
    }

    @Override
    public boolean insert(T entity) {
        Session session = openSession();
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        closeSession();
        return true;
    }

    @Override
    public boolean delete(Where where) {
        Session session = openSession();
        session.beginTransaction();
        String hql = "delete from " + getTableName() + (where == null ? "" : where);
        session.createQuery(hql, getEntityClass()).executeUpdate();
        session.getTransaction().commit();
        closeSession();
        return true;
    }

    @Override
    public boolean deleteById(long id) {
        Session session = openSession();
        session.beginTransaction();
        session.delete(queryById(id));
        session.getTransaction().commit();
        closeSession();
        return true;
    }

    @Override
    public boolean update(T entity) {
        Session session = openSession();
        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();
        closeSession();
        return true;
    }

    @Override
    public T queryById(long id) {
        Session session = openSession();
        T entity;
        switch (getIdFieldType().getSimpleName()) {
            case "int":
            case "Integer":
                entity = session.get(getEntityClass(), (int) id);
                break;
            case "long":
            case "Long":
                entity = session.get(getEntityClass(), id);
                break;
            default:
                throw new RuntimeException(getEntityClass().getName() + " : id must be number");
        }
        closeSession();
        return entity;
    }

    @Override
    public List<T> queryAll() {
        Session session = openSession();
        String hql = "from " + getTableName();
        List<T> entityList = session.createQuery(hql, getEntityClass()).list();
        closeSession();
        return entityList;
    }

    @Override
    public List<T> query(Where where) {
        Session session = openSession();
        String hql = "from " + getTableName() + (where == null ? "" : where);
        List<T> entityList = session.createQuery(hql, getEntityClass()).list();
        closeSession();
        return entityList;
    }

    @Override
    public List<T> query(Q q) {
        Where where = q.getWhere();
        String hql = "from " + getTableName() + (where == null ? "" : where);
        hql += Q.createOrderBy(q.getOrderBy());
        Session session = openSession();
        Query<T> query = session.createQuery(hql, getEntityClass());
        if (q.getOffset() >= 0 && q.getRowCount() > 0) {
            query.setFirstResult(q.getOffset());
            query.setMaxResults(q.getRowCount());
        }
        List<T> list = query.list();
        closeSession();
        return list;
    }

    @Override
    public int queryCount(Where where) {
        Session session = openSession();
        String hql = "select count(*) from " + getTableName();
        hql += where == null ? "" : where;
        Long count = (Long) session.createQuery(hql).uniqueResult();
        closeSession();
        return count.intValue();
    }

}
