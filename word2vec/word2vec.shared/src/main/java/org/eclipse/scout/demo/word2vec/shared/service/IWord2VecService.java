package org.eclipse.scout.demo.word2vec.shared.service;

import java.util.Collection;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IWord2VecService extends IService, WordVectors {
	Collection<String> wordsNearest(double[] word, int top);
	double similarity(double[] word1, String word2);
}
