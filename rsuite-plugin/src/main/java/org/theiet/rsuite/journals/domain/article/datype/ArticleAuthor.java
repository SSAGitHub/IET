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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((salutation == null) ? 0 : salutation.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArticleAuthor other = (ArticleAuthor) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (salutation == null) {
			if (other.salutation != null)
				return false;
		} else if (!salutation.equals(other.salutation))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}
	
	
		
}
