package com.nextlabs.enrollment.webapp.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.bluejungle.framework.comp.IComponentManager;
import com.bluejungle.framework.configuration.DestinyRepository;
import com.bluejungle.framework.datastore.hibernate.PoolBackedHibernateRepositoryImpl;
import com.nextlabs.utc.conf.EnrollmentConfComponentImpl;

public class EnrollmentSession {
	private PoolBackedHibernateRepositoryImpl repositary;
	private IComponentManager manager;
	private Session session;

	public void init() {
		repositary = (PoolBackedHibernateRepositoryImpl) manager
				.getComponent(DestinyRepository.DICTIONARY_REPOSITORY.getName());
		try {

			setSession(repositary.openSession());
		} catch (HibernateException e) {
			EnrollmentConfComponentImpl.log.info(e.toString());
		}
	}

	public PoolBackedHibernateRepositoryImpl getRepositary() {
		return repositary;
	}

	public void setRepositary(PoolBackedHibernateRepositoryImpl repositary) {
		this.repositary = repositary;
	}

	public IComponentManager getManager() {
		return manager;
	}

	public void setManager(IComponentManager manager) {
		this.manager = manager;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public ResultSet getResultSet(String query) {
		EnrollmentConfComponentImpl.log.info("Query:" + query);
		ResultSet rs = null;
		try {
			Connection conn = session.connection();
			EnrollmentConfComponentImpl.log.info("Connection:" + conn);
			if (conn != null && !conn.isClosed()) {
				Statement st = conn.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				rs = st.executeQuery(query);
			}
		} catch (HibernateException e) {
			EnrollmentConfComponentImpl.log.error(e.toString(), e);
		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log.error(e.toString(), e);
		}
		return rs;

	}

	public Statement getStatement() {
		Statement st = null;
		try {
			Connection conn = session.connection();
			EnrollmentConfComponentImpl.log.info("Connection:" + conn);
			if (conn != null && !conn.isClosed()) {
				st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);

			}
		} catch (HibernateException e) {
			EnrollmentConfComponentImpl.log.error(e.toString(), e);
		} catch (SQLException e) {
			EnrollmentConfComponentImpl.log.error(e.toString(), e);
		}
		return st;
	}
}
