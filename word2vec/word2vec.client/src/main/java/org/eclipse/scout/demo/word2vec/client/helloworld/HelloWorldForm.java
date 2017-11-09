package org.eclipse.scout.demo.word2vec.client.helloworld;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.scout.demo.word2vec.client.helloworld.HelloWorldForm.MainBox.TopBox;
import org.eclipse.scout.demo.word2vec.client.helloworld.HelloWorldForm.MainBox.TopBox.NearestWordsField;
import org.eclipse.scout.demo.word2vec.client.helloworld.HelloWorldForm.MainBox.TopBox.NearestWordsField.Table;
import org.eclipse.scout.demo.word2vec.client.helloworld.HelloWorldForm.MainBox.TopBox.WordBox.InVocabularyField;
import org.eclipse.scout.demo.word2vec.client.helloworld.HelloWorldForm.MainBox.TopBox.WordBox.TopField;
import org.eclipse.scout.demo.word2vec.client.helloworld.HelloWorldForm.MainBox.TopBox.WordBox.WordField;
import org.eclipse.scout.demo.word2vec.shared.helloworld.HelloWorldFormData;
import org.eclipse.scout.demo.word2vec.shared.service.IWord2VecService;
import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link HelloWorldForm}</h3>
 *
 * @author mzi
 */
@FormData(value = HelloWorldFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class HelloWorldForm extends AbstractForm {

	private static final Logger LOG = LoggerFactory.getLogger(HelloWorldForm.class);
	
	public HelloWorldForm() {
		setHandler(new ViewHandler());
	}

	@Override
	protected boolean getConfiguredAskIfNeedSave() {
		return false;
	}

	@Override
	protected int getConfiguredModalityHint() {
		return MODALITY_HINT_MODELESS;
	}

	@Override
	protected String getConfiguredIconId() {
		return AbstractIcons.World;
	}

	public MainBox getMainBox() {
		return getFieldByClass(MainBox.class);
	}

	public WordField getWordField() {
		return getFieldByClass(WordField.class);
	}

	public TopField getTopField() {
		return getFieldByClass(TopField.class);
	}

	public InVocabularyField getInVocabularyField() {
		return getFieldByClass(InVocabularyField.class);
	}

	public NearestWordsField getNearestWordsField() {
		return getFieldByClass(NearestWordsField.class);
	}

	public TopBox getTopBox() {
		return getFieldByClass(TopBox.class);
	}

	@Order(1000)
	public class MainBox extends AbstractGroupBox {

		@Order(1000)
		public class TopBox extends AbstractGroupBox {

			@Order(0)
			public class WordBox extends AbstractSequenceBox {

				@Override
				protected int getConfiguredGridW() {
					return 2;
				}

				@Override
				protected boolean getConfiguredAutoCheckFromTo() {
					return false;
				}

				@Order(1000)
				public class WordField extends AbstractStringField {

					@Override
					protected String getConfiguredLabel() {
						return TEXTS.get("WordPhrase");
					}

					@Override
					protected int getConfiguredGridW() {
						return 2;
					}

					@Override
					protected int getConfiguredMaxLength() {
						return 256;
					}
					
					@Override
					protected void execInitField() {
						checkValueEmpty();
					}
					
					public void checkValueEmpty() {
						String message = TEXTS.get("WordFieldMessage");
						addErrorStatus(new Status(message, Status.INFO));						
					}

					@Override
					protected void execChangedValue() {
						updateWordFields(getValue());
					}
				}

				@Order(2000)
				public class TopField extends AbstractLongField {
					@Override
					protected String getConfiguredLabel() {
						return TEXTS.get("Top");
					}

					@Override
					protected Long getConfiguredMinValue() {
						return 1L;
					}

					@Override
					protected Long getConfiguredMaxValue() {
						return 200L;
					}

					@Override
					protected void execInitField() {
						setValue(15L);
					}
				}

				@Order(3000)
				public class InVocabularyField extends AbstractBooleanField {
					@Override
					protected String getConfiguredLabel() {
						return TEXTS.get("InVocabulary");
					}

					@Override
					protected boolean getConfiguredEnabled() {
						return false;
					}
				}
			}

			@Order(4000)
			public class NearestWordsField extends AbstractTableField<NearestWordsField.Table> {

				@Override
				protected String getConfiguredLabel() {
					return TEXTS.get("NearestWords");
				}

				@Override
				protected int getConfiguredGridW() {
					return 2;
				}

				@Override
				protected int getConfiguredGridH() {
					return 6;
				}

				public class Table extends AbstractTable {

					public SimilarityColumn getSimilarityColumn() {
						return getColumnSet().getColumnByClass(SimilarityColumn.class);
					}

					public WordColumn getWordColumn() {
						return getColumnSet().getColumnByClass(WordColumn.class);
					}

					@Order(1000)
					public class WordColumn extends AbstractStringColumn {
						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("Word");
						}

						@Override
						protected int getConfiguredWidth() {
							return 300;
						}
					}

					@Order(2000)
					public class SimilarityColumn extends AbstractBigDecimalColumn {
						@Override
						protected String getConfiguredHeaderText() {
							return TEXTS.get("Similarity");
						}

						@Override
						protected int getConfiguredFractionDigits() {
							return 6;
						}

						@Override
						protected int getConfiguredWidth() {
							return 100;
						}
					}

					public void addRow(String word, double similarity) {
						Table table = getTable();
						ITableRow r;

						//First Row:
						r = table.addRow(getTable().createRow());
						table.getWordColumn().setValue(r, word);
						table.getSimilarityColumn().setValue(r, new BigDecimal(similarity));
					}
				}
			}

			/**
			 * Update the content of the fields depending on the input of the
			 * word/phrase field.
			 */
			private void updateWordFields(String word) {
				if(word == null || word.isEmpty()) {
					getWordField().checkValueEmpty();
					return;
				}
				
				IWord2VecService vec = BEANS.get(IWord2VecService.class);

				if(isSingleWord(word)) { 
					updateWithSingleWord(vec, word);
					logWordVector(vec, word);
				}
				else { 
					updateWithPhrase(vec, word); 
				}
			}

			private void logWordVector(IWord2VecService vec, String word) {
				double [] w = vec.getWordVector(word);
				StringBuffer b = new StringBuffer();
				
				for(double wi: w) {
					b.append(String.format(",%.3f", wi));
				}
				
				LOG.info(String.format("vector('%s'): [%s]", word, b.toString().substring(1)));
			}

			/**
			 * Returns true if the provided text corresponds to a single word.
			 * Returns false if the text represents a phrase calculation.
			 */
			private boolean isSingleWord(String text) {
				return !text.matches(".*[\\+\\-]+.*");
			}

			/**
			 * Updates the fields for the single word case.
			 */
			private void updateWithSingleWord(IWord2VecService vec, String word) {
				getWordField().clearErrorStatus();

				if(vec.hasWord(word)) {
					getInVocabularyField().setValue(true);
					double [] wordArray = vec.getWordVector(word);

					int top = getTopField().getValue().intValue();
					Collection<String> closeWords = vec.wordsNearest(wordArray, top);

					Table table = getNearestWordsField().getTable();
					table.deleteAllRows();

					for(String closeWord: closeWords) {
						double similarity = vec.similarity(word, closeWord);
						table.addRow(closeWord, similarity);
					}
				}
				else {
					String message = "Unkonwn word: " + word;
					getWordField().addErrorStatus(new Status(message, Status.WARNING));

					getInVocabularyField().setValue(false);
					getNearestWordsField().getTable().deleteAllRows();
				}
			}

			private void updateWithPhrase(IWord2VecService vec, String phrase) {
				getWordField().clearErrorStatus();

				String [] token = toToken(phrase);
				double [] word = new double [300];
				List<String> unk = new ArrayList<>();

				if(token.length == 0 || token.length % 2 == 0) {
					String message = "Phrase format error: Correct syntax:\n'[word] (<op> [word])+' with <op>={+,-}. eg. Rome - Italy + Germany";
					getWordField().addErrorStatus(new Status(message, Status.ERROR));
//					throw new ProcessingException(message);
				}

				getInVocabularyField().setValue(true);
				String firstWord = token[0];

				if(vec.hasWord(firstWord)) {
					word = vec.getWordVector(firstWord);
				}
				else {
					unk.add(firstWord);
					getInVocabularyField().setValue(true);
				}

				for(int i = 2; i < token.length; i += 2) {
					String opText = token[i-1];
					String otherWordText = token[i];

					if(vec.hasWord(otherWordText)) {
						double [] otherWordArray = vec.getWordVector(otherWordText);

						if("+".equals(opText))       { 
							word = addWord(word, otherWordArray, 1.0);
						}
						else if ("-".equals(opText)) { 
							word = addWord(word, otherWordArray, -1.0);
						}
						else {
							String message = "unknown <op> '" + opText + "' at position " + (i-1) + " expected '+' or '-'";
							getWordField().addErrorStatus(new Status(message, Status.ERROR));
						}
					}
					else {
						unk.add(otherWordText);
						getInVocabularyField().setValue(false);
					}
				}

				if(unk.size() > 0) {
					String message = "Unkonwn words: " + StringUtility.join(",", unk);
					getWordField().addErrorStatus(new Status(message, Status.WARNING));
				}

				int top = getTopField().getValue().intValue();
				Collection<String> closeWords = vec.wordsNearest(word, top);

				Table table = getNearestWordsField().getTable();
				table.deleteAllRows();

				for(String closeWord: closeWords) {
					double similarity = vec.similarity(word, closeWord);
					table.addRow(closeWord, similarity);
				}
			}
			
			/**
			 * Normalizes phrase and converts phrase to an array of tokens.
			 */
			private String [] toToken(String phrase) {
				phrase = phrase.replace("+", " + ");
				phrase = phrase.replace("-",  " - ");
				phrase = phrase.replaceAll("[ ]+", " ");

				return phrase.split("[ ]");
			}

			private double [] addWord(double [] word1, double [] word2, double factor) {
				double [] result = new double[300];

				for(int i = 0; i < 300; i++) {
					result[i] = word1[i] + factor * word2[i];
				}

				return result;
			}
		}
	}

	public class ViewHandler extends AbstractFormHandler {

		@Override
		protected void execLoad() {
			// noop
//			IHelloWorldService service = BEANS.get(IHelloWorldService.class);
//			HelloWorldFormData formData = new HelloWorldFormData();
//			exportFormData(formData);
//			formData = service.load(formData);
//			importFormData(formData);
		}
	}
}
