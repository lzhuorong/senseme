import numpy as np
import matplotlib.pyplot as plt
from sklearn.ensemble import ExtraTreesClassifier
from sklearn.pipeline import Pipeline
from sklearn import svm,cross_validation
from sklearn.ensemble import RandomForestClassifier
from sklearn.cross_validation import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.cross_validation import cross_val_score
from sklearn.metrics import confusion_matrix
from sklearn.svm import SVC
from sklearn.learning_curve import learning_curve
from sklearn.preprocessing import label_binarize
from sklearn.multiclass import OneVsRestClassifier
from sklearn.metrics import precision_recall_curve
from sklearn.metrics import average_precision_score
from sklearn.metrics import roc_curve, auc

# load data
file=open("../data/feature.txt")
data=np.loadtxt(file);
cols=data.shape[1];
x=data[:,0:cols-1];
y=data[:,-1];
#print x.shape[0],x.shape[1]
row=x.shape[0]
col=x.shape[1]
x_train,x_test,y_train,y_test=train_test_split(x,y,test_size=0.3,random_state=0)
# train and predict

clf=svm.SVC(kernel='linear',C=10).fit(x_train,y_train)
y_pred=clf.predict(x_test)
accuracy=accuracy_score(y_test,y_pred)
cm=confusion_matrix(y_test,y_pred)
print("single test:")
print("accuracy: %f" %(accuracy))
print(cm)

print("\n")

print("k-fold cross-validation test:")
res=cross_val_score(clf,x,y,cv=10)
cnt=0;
for acc in res:
    cnt+=1
    print("fold %d: %f" %(cnt,acc))