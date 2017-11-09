package org.eclipse.scout.demo.word2vec.shared.service;

import java.security.BasicPermission;

public class ReadWord2VecPermission extends BasicPermission {

	private static final long serialVersionUID = 1L;

	public ReadWord2VecPermission() {
		super(ReadWord2VecPermission.class.getSimpleName());
	}
}
