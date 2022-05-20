#!/bin/bash



# # check if java  environment variables is set
# if [ -z "${JAVA_HOME}" ]
# then
#     echo "ERROR: java not found"
#     exit -1
# fi



# fetching all programm folders from normal and optimized programs
cd "normal/"
normal_programs=($(find -maxdepth 1))
cd "../optimized/"
optimized_programs=($(find -maxdepth 1))
cd ".."


# check if the amount of normal and optimized programs is equal
num_normal_programs=${#normal_programs[@]}
num_optimized_programs=${#optimized_programs[@]}

if [ $num_normal_programs != $num_optimized_programs ]
then
    echo "ERROR: there are not the amount of normal an optimized programs"
    exit -1
fi


# check if the names of normal and optimized programs are equal
for (( i=1; i<=$num_normal_programs; i++ ))
do  
    if [ "${normal_programs[i]}" != "${optimized_programs[i]}" ]
    then
        printf "ERROR: %s and %s are not equal... stopping execution\n" "${normal_programs[i]}" "${optimized_programs[i]}"
        exit -1
    fi
done

mkdir -p diffs

cd "normal/"
for (( i=0; i<$num_normal_programs; i++ ))
do
    if [ "${normal_programs[i]}" != "." ] && [ "${normal_programs[i]}" != ".." ] && [ "${normal_programs[i]}" != "" ]
    then
        echo "${normal_programs[i]}" 
    	cd "${normal_programs[i]}"
        jova_files=($(ls *.j))
        java -jar ../../jasmin.jar Main.j $jova_files
        java -cp . Main > output.txt
        cd ".."
    fi
done

echo ""
echo "optimized..." 
echo ""


cd "../optimized/"
for (( i=0; i<$num_normal_programs; i++ ))
do
    if [ "${normal_programs[i]}" != "." ] && [ "${normal_programs[i]}" != ".." ] && [ "${normal_programs[i]}" != "" ]
    then
        echo "${normal_programs[i]}" 
    	cd "${normal_programs[i]}"
        jova_files=($(ls *.j))
        java -jar ../../jasmin.jar Main.j $jova_files
        java -cp . Main > output.txt
        diff output.txt ../../normal/${normal_programs[i]}/output.txt > ../../diffs/diff_${normal_programs[i]:2}.txt
        cd ".."
    fi
done


echo ""
echo "check diffs..." 
echo ""


cd "../diffs/"
diff_files=($(ls))
num_diff_files=${#diff_files[@]}



for (( i=0; i<=$num_diff_files; i++ ))
do
    if [ "${diff_files[i]}" != "." ] && [ "${diff_files[i]}" != ".." ] && [ "${diff_files[i]}" != "" ]
    then
        
        file_size=$(stat -c%s "${diff_files[i]}")
        if [[ $file_size != 0 ]] 
        then
            echo ""
            echo "ERROR: ${diff_files[i]} differ" 
            echo ""
        else
            echo "OK: ${diff_files[i]}" 
        fi
    fi
done
