package com.wongnai.interview.movie;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> actors = new ArrayList<>();

	/**
	 * Required by JPA.
	 */
	protected Movie() {
	}

	public Movie(String name, List<String> actors) {
		this.name = name;
		this.actors = actors;
	}

	public Movie(Long id, String name, List<String> actors) {
		this.id = id;
		this.name = name;
		this.actors = actors;
	}

	public Movie(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getActors() {
		return actors;
	}
}
