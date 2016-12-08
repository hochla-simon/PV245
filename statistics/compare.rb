# Befor start need to copy to tfidf.rb and keywords.rb scripts 
# and source data like pagefeed.json or events.json
# otherwise make change in code under.

require "json"
require "i18n"


keywords = JSON.parse `ruby keywords.rb 10 pagesfeed.json`
tfidf = JSON.parse `ruby tfidf.rb 10 pagesfeed.json`
I18n.config.available_locales = :en

tfidf.each do |k| 
	k["words"] = k["words"].map {|v| I18n.transliterate v}
end
x = tfidf.size

for i in 0..x-1
	inter =  (keywords[i]["words"] & tfidf[i]["words"]) 
	puts "#{i}, #{inter.size}, #{inter}"
	# p tfidf[i]["words"] - inter
end
