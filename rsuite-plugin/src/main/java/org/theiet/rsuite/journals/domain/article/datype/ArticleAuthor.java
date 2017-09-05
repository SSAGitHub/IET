package org.theiet.rsuite.journals.domain.article.datype;


public class ArticleAuthor {

	private String email;
	
	private String salutation;
	
	private String surname;
	
	private String firstname;

	public ArticleAuthor(String salutation, String firstname, String surname,
			String email) {
		super();
		this.salutation = salutation;
		this.firstname = firstname;
		this.surname = surname;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getSalutation() {
		return salutation;
	}

	public String getSurname() {
		return surname;
	}

	public String getFirstname() {
		return firstname;
	}
		
}
