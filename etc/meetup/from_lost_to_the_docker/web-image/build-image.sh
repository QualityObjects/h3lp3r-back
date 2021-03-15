docker build -t h3lp3r-web-tmp -t qualityobjects/h3lp3r-web-tmp --rm -f ./Dockerfile .

#For testing
# docker run -it --rm -p 1080:80 h3lp3r-web-tmp

#To deploy in PRO
# docker run -it --name h3lper-web-tmp -p 1080:80 -d h3lp3r-web-tmp

#To remove it from PRO
# docker stop h3lper-web-tmp
# docker rm h3lper-web-tmp


