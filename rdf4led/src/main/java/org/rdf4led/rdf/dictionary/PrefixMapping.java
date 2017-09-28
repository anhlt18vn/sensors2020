package org.rdf4led.rdf.dictionary;

public interface PrefixMapping<PREFIX, URI> {

    public PrefixMapping<PREFIX, URI> addPrefix(PREFIX prefix, URI uri);

    public PrefixMapping<PREFIX, URI> removePrefix(PREFIX prefix);

    public URI getURI(PREFIX prefix);

    public PREFIX getPrefix(URI uri);
}
