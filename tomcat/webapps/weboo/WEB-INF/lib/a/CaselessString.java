package a;
import java.util.*;
public final class CaselessString {
  private final String string;
  private final String normalized;

  private CaselessString(String string, Locale locale) {
    this.string = string;
    normalized = string.toUpperCase(locale);
  }

  @Override public String toString() { return string; }

  @Override public int hashCode() { return normalized.hashCode(); }

  @Override public boolean equals(Object obj) {
    if (obj instanceof CaselessString) {
      return ((CaselessString) obj).normalized.equals(normalized);
    }
    return false;
  }

  public static CaselessString as(String s, Locale locale) {
    return new CaselessString(s, locale);
  }

  public static CaselessString as(String s) {
    return as(s, Locale.ENGLISH);
  }
}