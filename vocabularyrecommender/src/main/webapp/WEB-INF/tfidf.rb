#!/usr/bin/env ruby
# encoding: UTF-8
# coding: UTF-8

def basedir
  File.dirname(__FILE__)
end

require File.join(basedir,'tf-idf-lib.rb')
require 'json'
require "pp"
# require 'active_support/inflector'


filename = 'inputExample.json'
filename = ARGV[1] if ARGV[1]

limit = 5 #restrict to the top 3 relevant words per document
limit = ARGV[0].to_i if ARGV[0]

def whatLanguage txt 
    if txt.include? 'and'
		:en
	elsif txt.include? 'sa'
		:sk
	elsif txt.include? 'se'
		:cs
	else
		:en
	end
end

def tfidf(filename, limit)

	if !File.exist?(filename)
		puts "#{filename} not exists!!!!"
		exit
	end
	file = File.read(filename,:encoding => "utf-8")

	texts = JSON.parse(file)

	corpus =  Array.new
	texts.each { |event|
		corpus << event.values[0] }

    corpus.map! {|text| text.gsub(/[0-9\-–★:!@%&.,?><\/}{(~_)"#$\*\#]/," ")} 
    
	exclude_stop_words = true
	@t = RubyTfIdf::TfIdf.new(corpus, limit, exclude_stop_words)
	output =  @t.tf_idf
	
	i = 0;
	final = Array.new

	output.each do |my_hash|
		pom = Hash.new
		pom[:event_name] = texts[i].keys.first;
		pom[:language] = whatLanguage corpus[i]
		pom[:words] = my_hash.keys
		final << pom
		i += 1
	end

	json_str = final.to_json
	# json_str = JSON.pretty_generate final
	File.open("tfidf_output.json", 'w') { |file| file.write(json_str) }

	return json_str
end

puts tfidf(filename, limit)
