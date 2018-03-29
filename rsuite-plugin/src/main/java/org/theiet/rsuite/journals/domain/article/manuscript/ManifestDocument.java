package org.theiet.rsuite.journals.domain.article.manuscript;

import java.util.List;

import org.theiet.rsuite.journals.domain.article.datype.ArticleAuthor;

public class ManifestDocument {

	private String articleId;

	private boolean supplementaryMaterials;

	private String articleTitle;

	private String manuscriptType;

	private ArticleAuthor author;

	private boolean isSpecialIssue;

	private String specialIssueTitle;

	private boolean openAcessCheckList;

	private String category;

	private List<String> classifications;

	private String submittedDate;

	private String decisionDate;

	private String exportDate;

	private String licenseType;

	private String submissionType;

	private String categoryCodes;
	
	private ManifestType manifestType;

	public String getArticleTitle() {
		return articleTitle;
	}

	public String getArticleId() {
		return articleId;
	}

	public boolean getSupplementaryMaterial() {
		return supplementaryMaterials;
	}

	public String getManuscriptType() {
		return manuscriptType;
	}

	public ArticleAuthor getArticleAuthor() {
		return author;
	}

	public boolean isSpecialIssue() {
		return isSpecialIssue;
	}

	public String getSpecialIssueTitle() {
		return specialIssueTitle;
	}

	public boolean isOpenAccessCheckList() {
		return openAcessCheckList;
	}

	public String getCategory() {
		return category;
	}

	public List<String> getClassifications() {
		return classifications;
	}

	public String getSubmittedDate() {
		return fixIfEmptyDate(submittedDate);
	}

	public String getDecisionDate() {
		return fixIfEmptyDate(decisionDate);
	}

	private String fixIfEmptyDate(String date) {
		if ("--".equals(date)) {
			date = "";
		}
		return date;
	}

	public String getExportDate() {
		return exportDate;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public String getSubmissionType() {
		return submissionType;
	}

	public String getCategoryCodes() {
		return categoryCodes;
	}
	
	

	public ManifestType getManifestType() {
		return manifestType;
	}



	public static class Builder {
		private String articleId;
		private boolean supplementaryMaterials;
		private String articleTitle;
		private String manuscriptType;
		private ArticleAuthor author;
		private boolean isSpecialIssue;
		private String specialIssueTitle;
		private boolean openAcessCheckList;
		private String category;
		private List<String> classifications;
		private String submittedDate;
		private String decisionDate;
		private String exportDate;
		private String licenseType;
		private String submissionType;
		private String categoryCodes;
		private ManifestType manifestType;
		
		public Builder(ManifestType manifestType) {
			this.manifestType = manifestType;
		}

		public Builder articleId(String articleId) {
			this.articleId = articleId;
			return this;
		}

		public Builder supplementaryMaterials(boolean supplementaryMaterials) {
			this.supplementaryMaterials = supplementaryMaterials;
			return this;
		}

		public Builder articleTitle(String articleTitle) {
			this.articleTitle = articleTitle;
			return this;
		}

		public Builder manuscriptType(String manuscriptType) {
			this.manuscriptType = manuscriptType;
			return this;
		}

		public Builder author(ArticleAuthor author) {
			this.author = author;
			return this;
		}

		public Builder isSpecialIssue(boolean isSpecialIssue) {
			this.isSpecialIssue = isSpecialIssue;
			return this;
		}

		public Builder specialIssueTitle(String specialIssueTitle) {
			this.specialIssueTitle = specialIssueTitle;
			return this;
		}

		public Builder openAcessCheckList(boolean openAcessCheckList) {
			this.openAcessCheckList = openAcessCheckList;
			return this;
		}

		public Builder category(String category) {
			this.category = category;
			return this;
		}
		
		public Builder manifestType(ManifestType manifestType) {
			this.manifestType = manifestType;
			return this;
		}

		public Builder classifications(List<String> classifications) {
			this.classifications = classifications;
			return this;
		}

		public Builder submittedDate(String submittedDate) {
			this.submittedDate = submittedDate;
			return this;
		}

		public Builder decisionDate(String decisionDate) {
			this.decisionDate = decisionDate;
			return this;
		}

		public Builder exportDate(String exportDate) {
			this.exportDate = exportDate;
			return this;
		}

		public Builder licenseType(String licenseType) {
			this.licenseType = licenseType;
			return this;
		}

		public Builder submissionType(String submissionType) {
			this.submissionType = submissionType;
			return this;
		}

		public Builder categoryCodes(String categoryCodes) {
			this.categoryCodes = categoryCodes;
			return this;
		}

		public ManifestDocument build() {
			return new ManifestDocument(this);
		}
	}

	private ManifestDocument(Builder builder) {
		this.articleId = builder.articleId;
		this.supplementaryMaterials = builder.supplementaryMaterials;
		this.articleTitle = builder.articleTitle;
		this.manuscriptType = builder.manuscriptType;
		this.author = builder.author;
		this.isSpecialIssue = builder.isSpecialIssue;
		this.specialIssueTitle = builder.specialIssueTitle;
		this.openAcessCheckList = builder.openAcessCheckList;
		this.category = builder.category;
		this.classifications = builder.classifications;
		this.submittedDate = builder.submittedDate;
		this.decisionDate = builder.decisionDate;
		this.exportDate = builder.exportDate;
		this.licenseType = builder.licenseType;
		this.submissionType = builder.submissionType;
		this.categoryCodes = builder.categoryCodes;
		this.manifestType = builder.manifestType;
	}
}