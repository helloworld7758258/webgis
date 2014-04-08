package com.hnee.webgis.server;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "M_PLANUNG", uniqueConstraints = @UniqueConstraint(columnNames = "FKEY"))
@SecondaryTable(name = "MEASURES_COUNT", pkJoinColumns = { @PrimaryKeyJoinColumn(name = "FKEY", referencedColumnName = "PKEY") })
public class MPlanung {

	// Fields

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PKEY", unique = true, nullable = false)
	private Long pkey;

	@OneToOne
	@JoinColumn(name="FKEY")
	private Linfos linfos;
	
	@Column(table = "MEASURES_COUNT", name = "CNT")
	private Long measurecount;
	
	// Constructors

	/** default constructor */
	public MPlanung() {
	}

	// Property accessors
	
	public Long getPkey() {
		return this.pkey;
	}

	public void setPkey(Long pkey) {
		this.pkey = pkey;
	}

	public Linfos getLinfos() {
		return linfos;
	}

	public void setLinfos(Linfos linfos) {
		this.linfos = linfos;
	}

	public Long getMeasurecount() {
		return measurecount;
	}

	public void setMeasurecount(Long measurecount) {
		this.measurecount = measurecount;
	}

}