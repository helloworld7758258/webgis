package com.hnee.webgis.server;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "LINFOS", uniqueConstraints = @UniqueConstraint(columnNames = "GISPADID"))
public class Linfos {

	// Fields

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PKEY", unique = true, nullable = false)
	private Long pkey;
	
	@Column
	private Integer gispadid;
	
	@OneToOne
	@JoinColumn(name="GISPADID")
	private Geoobjekt geoobjekt;
	
	@OneToOne(mappedBy="linfos", cascade=CascadeType.ALL)
	private MPlanung mplanung;

	// Constructors

	/** default constructor */
	public Linfos() {
	}

	// Property accessors
	public Long getPkey() {
		return this.pkey;
	}

	public void setPkey(Long pkey) {
		this.pkey = pkey;
	}

	public Integer getGispadid() {
		return gispadid;
	}

	public void setGispadid(Integer gispadid) {
		this.gispadid = gispadid;
	}

	public Geoobjekt getGeoobjekt() {
		return geoobjekt;
	}

	public void setGeoobjekt(Geoobjekt geoobjekt) {
		this.geoobjekt = geoobjekt;
	}

}