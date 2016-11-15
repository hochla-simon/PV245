#!/usr/bin/env ruby

require './tfidf_lib/ruby-tf-idf'
require 'json'
require 'active_support/inflector'
require 'cld'

filename = 'inputExample.json'
filename = ARGV[1] if ARGV[1]

limit = 3 #restrict to the top 3 relevant words per document
limit = ARGV[0].to_i if ARGV[0]

def tfidf(filename, limit)
	file = File.read(filename)
	corpus = JSON.parse(file)
	
	corpus.map! {|text| text.parameterize.underscore.humanize} 
	exclude_stop_words = true

	@t = RubyTfIdf::TfIdf.new(corpus, limit, exclude_stop_words)
	output =  @t.tf_idf
	i = 0;
	output.each do |my_hash|
		#my_hash.each { |k, v| my_hash[k] = v.round(2) }
		my_hash.each { |k, v| my_hash[k] = CLD.detect_language(corpus[i])[:code] }
		i += 1
	end 

	json_str = output.to_json
	File.open("tfidf_output.json", 'w') { |file| file.write(json_str) }

	return json_str
end

puts tfidf(filename, limit)