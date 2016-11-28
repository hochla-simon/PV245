#!/usr/bin/env ruby

require './tfidf_lib/ruby-tf-idf'
require 'json'


filename = 'inputExample.json'
filename = ARGV[1] if ARGV[1]

limit = 1 #restrict to the top 3 relevant words per document
limit = ARGV[0].to_i if ARGV[0]

def whatLanguage txt 
	if txt.include? 'sa'
		:sk
	elsif txt.include? 'se'
		:cz
	elsif txt.include? 'and'
		:en
	else
		:en
	end
end

def normalize str
	str.tr(
	"ÀÁÂÃÄÅàáâãäåĀāĂăĄąÇçĆćĈĉĊċČčÐðĎďĐđÈÉÊËèéêëĒēĔĕĖėĘęĚěĜĝĞğĠġĢģĤĥĦħÌÍÎÏìíîïĨĩĪīĬĭĮįİıĴĵĶķĸĹĺĻļĽľĿŀŁłÑñŃńŅņŇňŉŊŋÒÓÔÕÖØòóôõöøŌōŎŏŐőŔŕŖŗŘřŚśŜŝŞşŠšſŢţŤťŦŧÙÚÛÜùúûüŨũŪūŬŭŮůŰűŲųŴŵÝýÿŶŷŸŹźŻżŽž",
	"AAAAAAaaaaaaAaAaAaCcCcCcCcCcDdDdDdEEEEeeeeEeEeEeEeEeGgGgGgGgHhHhIIIIiiiiIiIiIiIiIiJjKkkLlLlLlLlLlNnNnNnNnnNnOOOOOOooooooOoOoOoRrRrRrSsSsSsSssTtTtTtUUUUuuuuUuUuUuUuUuUuWwYyyYyYZzZzZz")
end 

def tfidf(filename, limit)
	file = File.read(filename)
	corpus = JSON.parse(file)
	 
    corpus.map! {|text| text.gsub(/[0-9\-!@%&.,?><\/}{()"#$\*]/,"")} 
    corpus.map! {|text| normalize text} 
    #corpus.map! {|text| text.parameterize.underscore.humanize} 


	exclude_stop_words = true
	@t = RubyTfIdf::TfIdf.new(corpus, limit, exclude_stop_words)
	output =  @t.tf_idf
	i = 0;
	output.each do |my_hash|

		my_hash.each { |k, v| my_hash[k] = whatLanguage corpus[i] }
		# my_hash.each { |k, v| my_hash[k] = CLD.detect_language(corpus[i])[:code] }
		i += 1
	end 

	json_str = output.to_json
	File.open("tfidf_output.json", 'w') { |file| file.write(json_str) }

	return json_str
end

puts tfidf(filename, limit)
