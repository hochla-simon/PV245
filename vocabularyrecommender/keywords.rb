#!/usr/bin/env ruby
# encoding: UTF-8

# require 'rubygems'
require 'highscore'
require 'json'
require "i18n"

def basedir
  File.dirname(__FILE__)
end

@blacklistCZ  = Highscore::Blacklist.load_file File.join(basedir, 'stopwords/stopwords_cz.txt')
@blacklistENG = Highscore::Blacklist.load_file File.join(basedir, "stopwords/stopwords_en.txt")
@blacklistSK  = Highscore::Blacklist.load_file File.join(basedir, "stopwords/stopwords_sk.txt")

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


def what_blacklist(text)
	# case CLD.detect_language(text)[:code]
	case whatLanguage(text)
	when :cs
		return @blacklistCZ
	when :sk
		return @blacklistSK
	else
		return @blacklistENG
	end
end

def count_keywords(text, blacklist)
	keywords = text.keywords(blacklist) do
	  set :multiplier, 10
	  # set :upper_case, 3
	  # set :long_words, 3
	  # set :long_words_threshold, 15
	  set :short_words_threshold, 3     # => default: 2
	  set :bonus_multiplier, 2         # => default: 3
	  set :vowels, 1                   # => default: 0 = not considered
	  set :consonants, 5                 # => default: 0 = not considered
	  set :ignore_case, true             # => default: false
	  set :word_pattern, /[\w]+[^\s0-9]/ # => default: /\w+/
	  set :stemming, false                # => default: false
	end
	return keywords
end

filename = 'inputExample.json'
filename = ARGV[1] if ARGV[1]

limit = 5
limit = ARGV[0].to_i if ARGV[0]

I18n.config.available_locales = :en

def get_keywords(filename, limit)
	if !File.exist?(filename)
	  puts "#{filename} not exists!!!!"
	  exit
	end

	file = File.read(filename,:encoding => "utf-8")
	texts = JSON.parse(file)

	corpus =  Array.new
	texts.each { |event|
	  corpus << event.values[0] }

	final = []
	i = 0
	corpus.each do |text|

	  blacklist = what_blacklist(text)
	  text = I18n.transliterate text.gsub(/[0-9\-–★:!@%&.,?><\/}{(~_)"#$\*]/," ")
	  keywords = count_keywords(text, blacklist)

	  pom = Hash.new
	  pom[:event_name] = texts[i].keys[0];
	  pom[:language] = whatLanguage text
	  pom[:words] = keywords.top(limit)
	  i += 1

	  final << pom
	end

	json_str = JSON.pretty_generate final
	File.open("keyword_output.json", 'w') { |file| file.write(json_str) }
	return json_str
end

puts get_keywords(filename, limit)

