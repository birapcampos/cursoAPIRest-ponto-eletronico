package br.com.cursoapirest.response;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

	private T data;
	private List<T> datalist;
	private List<String> errors;

	public Response() {
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<String> getErrors() {
		if (this.errors == null) {
			this.errors = new ArrayList<String>();
		}
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<T> getdatalist() {
		return datalist;
	}

	public void setdatalist(List<T> collecao) {
		this.datalist = collecao;
	}

	
}
