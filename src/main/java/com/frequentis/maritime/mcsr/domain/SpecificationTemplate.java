/*
 * MaritimeCloud Service Registry
 * Copyright (c) 2016 Frequentis AG
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.frequentis.maritime.mcsr.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import io.swagger.annotations.ApiModel;

import com.frequentis.maritime.mcsr.domain.enumeration.SpecificationTemplateType;

/**
 * A SpecificationTemplate contains information on how to define a aspects of
 * a service.It has a type do differentiate between e.g. logical definitions and
 * concrete service instances.Templates will evolve, that's why they have a version.
 *
 */
@ApiModel(description = ""
    + "A SpecificationTemplate contains information on how to define a aspects ofa service.It has a type do differentiate between e.g. "
    + "logical definitions andconcrete service instances.Templates will evolve, that's why they have a version.")
@Entity
@Table(name = "specification_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "specificationtemplate")
public class SpecificationTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    @Field(type = FieldType.text, index = true, fielddata = true)
    private String name;

    @NotNull
    @Column(name = "version", nullable = false)
    @Field(type = FieldType.text, index = true, fielddata = true)
    private String version;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SpecificationTemplateType type;

    @Column(name = "comment")
    @Field(type = FieldType.text, index = true, fielddata = true)
    private String comment;

    @ManyToOne
    private Doc guidelineDoc;

    @ManyToOne
    private Doc templateDoc;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "specification_template_docs",
               joinColumns = @JoinColumn(name="specification_templates_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="docs_id", referencedColumnName="ID"))
    private Set<Doc> docs = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "specification_template_xsds",
               joinColumns = @JoinColumn(name="specification_templates_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="xsds_id", referencedColumnName="ID"))
    private Set<Xsd> xsds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SpecificationTemplateType getType() {
        return type;
    }

    public void setType(SpecificationTemplateType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Doc getGuidelineDoc() {
        return guidelineDoc;
    }

    public void setGuidelineDoc(Doc doc) {
        this.guidelineDoc = doc;
    }

    public Doc getTemplateDoc() {
        return templateDoc;
    }

    public void setTemplateDoc(Doc doc) {
        this.templateDoc = doc;
    }

    public Set<Doc> getDocs() {
        return docs;
    }

    public void setDocs(Set<Doc> docs) {
        this.docs = docs;
    }

    public Set<Xsd> getXsds() {
        return xsds;
    }

    public void setXsds(Set<Xsd> xsds) {
        this.xsds = xsds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpecificationTemplate specificationTemplate = (SpecificationTemplate) o;
        if(specificationTemplate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, specificationTemplate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SpecificationTemplate{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", version='" + version + "'" +
            ", type='" + type + "'" +
            ", comment='" + comment + "'" +
            '}';
    }
}
