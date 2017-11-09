package org.eclipse.scout.demo.word2vec.shared.service;

import java.security.BasicPermission;

public class UpdateWord2VecPermission extends BasicPermission {

	private static final long serialVersionUID = 1L;

	public UpdateWord2VecPermission() {
		super(UpdateWord2VecPermission.class.getSimpleName());
	}
}
