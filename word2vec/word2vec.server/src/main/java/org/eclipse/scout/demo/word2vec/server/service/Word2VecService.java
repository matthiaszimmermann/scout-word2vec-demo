package org.eclipse.scout.demo.word2vec.server.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.reader.ModelUtils;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.eclipse.scout.demo.word2vec.shared.service.IWord2VecService;
import org.eclipse.scout.rt.platform.CreateImmediately;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CreateImmediately
public class Word2VecService implements IWord2VecService {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(Word2VecService.class);

	/** Compressed Google News vector file (1.3GB) */
	public static final String WORD_VECTORS_PATH = "C:\\eclipse\\data\\"
			+ "GoogleNews-vectors-negative300.bin.gz";
	
	/** The Word2vec model (loaded at startup) */
	private static Word2Vec vec;

	@PostConstruct
	private void init() {
		LOG.info("Loading word2vec model from " + WORD_VECTORS_PATH + "...");
		vec = WordVectorSerializer.readWord2VecModel(WORD_VECTORS_PATH);
		LOG.info("Loading word2vec model successfully completed");
	}

	/** Returns word vector for specified string */
	@Override
	public double[] getWordVector(String word) {
		assertModelLoaded();
		return vec.getWordVector(word);
	}

	/** Returns the words nearest to the provided word vector */
	@Override
	public Collection<String> wordsNearest(double[] word, int top) {
		return vec.wordsNearest(toINDArray(word), top);
	}
	
	private INDArray toINDArray(double [] array) {
		INDArray indArray = new NDArray(1, array.length);
		for(int i = 0; i < array.length; i++) {
			indArray.put(0, i, array[i]);
		}
		return indArray;
	}
	
	@Override
	public String getUNK() {
		assertModelLoaded();
		return vec.getUNK();
	}

	@Override
	public void setUNK(String newUNK) {
		assertModelLoaded();
		vec.setUNK(newUNK);
	}


	@Override
	public boolean hasWord(String word) {
		assertModelLoaded();
		return vec.hasWord(word);
	}


	@Override
	public Collection<String> wordsNearest(INDArray words, int top) {
		assertModelLoaded();
		return vec.wordsNearest(words, top);
	}


	@Override
	public Collection<String> wordsNearestSum(INDArray words, int top) {
		assertModelLoaded();
		return vec.wordsNearestSum(words, top);
	}


	@Override
	public Collection<String> wordsNearestSum(String word, int n) {
		assertModelLoaded();
		return vec.wordsNearest(word, n);
	}


	@Override
	public Collection<String> wordsNearestSum(Collection<String> positive, Collection<String> negative, int top) {
		assertModelLoaded();
		return vec.wordsNearestSum(positive, negative, top);
	}


	@Override
	public Map<String, Double> accuracy(List<String> questions) {
		assertModelLoaded();
		return vec.accuracy(questions);
	}


	@Override
	public int indexOf(String word) {
		assertModelLoaded();
		return vec.indexOf(word);
	}


	@Override
	public List<String> similarWordsInVocabTo(String word, double accuracy) {
		assertModelLoaded();
		return vec.similarWordsInVocabTo(word, accuracy);
	}


	@Override
	public INDArray getWordVectorMatrixNormalized(String word) {
		assertModelLoaded();
		return vec.getWordVectorMatrixNormalized(word);
	}


	@Override
	public INDArray getWordVectorMatrix(String word) {
		assertModelLoaded();
		return vec.getWordVectorMatrix(word);
	}


	@Override
	public INDArray getWordVectors(Collection<String> labels) {
		assertModelLoaded();
		return vec.getWordVectors(labels);
	}


	@Override
	public INDArray getWordVectorsMean(Collection<String> labels) {
		assertModelLoaded();
		return vec.getWordVectorsMean(labels);
	}


	@Override
	public Collection<String> wordsNearest(Collection<String> positive, Collection<String> negative, int top) {
		assertModelLoaded();
		return vec.wordsNearest(positive, negative, top);
	}


	@Override
	public Collection<String> wordsNearest(String word, int n) {
		assertModelLoaded();
		return vec.wordsNearestSum(word, n);
	}


	@Override
	public double similarity(String word, String word2) {
		assertModelLoaded();
		return vec.similarity(word, word2);
	}


	@SuppressWarnings("rawtypes")
	@Override
	public VocabCache vocab() {
		assertModelLoaded();
		return vec.vocab();
	}


	@SuppressWarnings("rawtypes")
	@Override
	public WeightLookupTable lookupTable() {
		assertModelLoaded();
		return vec.lookupTable();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setModelUtils(ModelUtils utils) {
		assertModelLoaded();
		vec.setModelUtils(utils);
	}
	
	@Override
	public double similarity(double[] word1, String word2) {
		assertModelLoaded();
		
		INDArray vec1 = toINDArray(word1);
		INDArray vec2 = vec.getWordVectorMatrix(word2);
		
		return Transforms.cosineSim(vec1, vec2);
	}
	
	private void assertModelLoaded() {
		// todo
	}
}
