/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package com.at.avro.standalone.dto;  
@SuppressWarnings("all")
/** A pair of strings, sorted by the 'right' field descendingly. */
@org.apache.avro.specific.AvroGenerated
public class StringPair extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"StringPair\",\"namespace\":\"com.at.avro.standalone.dto\",\"doc\":\"A pair of strings, sorted by the 'right' field descendingly.\",\"fields\":[{\"name\":\"left\",\"type\":\"string\",\"order\":\"ignore\"},{\"name\":\"right\",\"type\":\"string\",\"order\":\"descending\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence left;
  @Deprecated public java.lang.CharSequence right;

  /**
   * Default constructor.
   */
  public StringPair() {}

  /**
   * All-args constructor.
   */
  public StringPair(java.lang.CharSequence left, java.lang.CharSequence right) {
    this.left = left;
    this.right = right;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return left;
    case 1: return right;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: left = (java.lang.CharSequence)value$; break;
    case 1: right = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'left' field.
   */
  public java.lang.CharSequence getLeft() {
    return left;
  }

  /**
   * Sets the value of the 'left' field.
   * @param value the value to set.
   */
  public void setLeft(java.lang.CharSequence value) {
    this.left = value;
  }

  /**
   * Gets the value of the 'right' field.
   */
  public java.lang.CharSequence getRight() {
    return right;
  }

  /**
   * Sets the value of the 'right' field.
   * @param value the value to set.
   */
  public void setRight(java.lang.CharSequence value) {
    this.right = value;
  }

  /** Creates a new StringPair RecordBuilder */
  public static com.at.avro.standalone.dto.StringPair.Builder newBuilder() {
    return new com.at.avro.standalone.dto.StringPair.Builder();
  }
  
  /** Creates a new StringPair RecordBuilder by copying an existing Builder */
  public static com.at.avro.standalone.dto.StringPair.Builder newBuilder(com.at.avro.standalone.dto.StringPair.Builder other) {
    return new com.at.avro.standalone.dto.StringPair.Builder(other);
  }
  
  /** Creates a new StringPair RecordBuilder by copying an existing StringPair instance */
  public static com.at.avro.standalone.dto.StringPair.Builder newBuilder(com.at.avro.standalone.dto.StringPair other) {
    return new com.at.avro.standalone.dto.StringPair.Builder(other);
  }
  
  /**
   * RecordBuilder for StringPair instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<StringPair>
    implements org.apache.avro.data.RecordBuilder<StringPair> {

    private java.lang.CharSequence left;
    private java.lang.CharSequence right;

    /** Creates a new Builder */
    private Builder() {
      super(com.at.avro.standalone.dto.StringPair.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(com.at.avro.standalone.dto.StringPair.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing StringPair instance */
    private Builder(com.at.avro.standalone.dto.StringPair other) {
            super(com.at.avro.standalone.dto.StringPair.SCHEMA$);
      if (isValidValue(fields()[0], other.left)) {
        this.left = data().deepCopy(fields()[0].schema(), other.left);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.right)) {
        this.right = data().deepCopy(fields()[1].schema(), other.right);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'left' field */
    public java.lang.CharSequence getLeft() {
      return left;
    }
    
    /** Sets the value of the 'left' field */
    public com.at.avro.standalone.dto.StringPair.Builder setLeft(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.left = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'left' field has been set */
    public boolean hasLeft() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'left' field */
    public com.at.avro.standalone.dto.StringPair.Builder clearLeft() {
      left = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'right' field */
    public java.lang.CharSequence getRight() {
      return right;
    }
    
    /** Sets the value of the 'right' field */
    public com.at.avro.standalone.dto.StringPair.Builder setRight(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.right = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'right' field has been set */
    public boolean hasRight() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'right' field */
    public com.at.avro.standalone.dto.StringPair.Builder clearRight() {
      right = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public StringPair build() {
      try {
        StringPair record = new StringPair();
        record.left = fieldSetFlags()[0] ? this.left : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.right = fieldSetFlags()[1] ? this.right : (java.lang.CharSequence) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
