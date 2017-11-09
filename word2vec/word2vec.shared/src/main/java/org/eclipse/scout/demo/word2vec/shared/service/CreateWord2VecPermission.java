package org.eclipse.scout.demo.word2vec.shared.service;

import java.security.BasicPermission;

public class CreateWord2VecPermission extends BasicPermission {

	private static final long serialVersionUID = 1L;

	public CreateWord2VecPermission() {
		super(CreateWord2VecPermission.class.getSimpleName());
	}
}
