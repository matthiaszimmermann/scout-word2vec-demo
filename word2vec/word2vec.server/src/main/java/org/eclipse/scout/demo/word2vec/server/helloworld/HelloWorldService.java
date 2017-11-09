package org.eclipse.scout.demo.word2vec.server.helloworld;

import org.eclipse.scout.demo.word2vec.shared.helloworld.HelloWorldFormData;
import org.eclipse.scout.demo.word2vec.shared.helloworld.IHelloWorldService;

/**
 * <h3>{@link HelloWorldService}</h3>
 *
 * @author mzi
 */
public class HelloWorldService implements IHelloWorldService {

	@Override
	public HelloWorldFormData load(HelloWorldFormData input) {
		return input;
	}
}
