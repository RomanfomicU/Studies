# -*- coding: utf-8 -*-
"""prak4.ipynb

Automatically generated by Colab.

Original file is located at
    https://colab.research.google.com/drive/1cbJQr0eOPielRopRHM9V97v4h_K3_6ti
"""

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import scipy
import statsmodels.api as sm
from statsmodels.formula.api import ols
from scipy.stats import ttest_ind
from statsmodels.stats.multitest import multipletests
from statsmodels.stats.multicomp import pairwise_tukeyhsd
from statsmodels.stats.anova import anova_lm

"""# Задание 1"""

data = {
    'День': ['Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница'],
    'Улица': [80, 98, 75, 91, 78],
    'Гараж': [100, 82, 105, 89, 102]
}

df = pd.DataFrame(data)
correlation = df['Улица'].corr(df['Гараж'])
print(f'Корреляция по Пирсону между улицей и гаражом: {correlation}')

plt.scatter(df['Улица'], df['Гараж'])
plt.xlabel('Автомобили на Улице')
plt.ylabel('Автомобили в Гараже')
plt.title('Диаграмма рассеяния между Улицей и Гаражом')
plt.show()

"""# Задание 2"""

df = pd.read_csv('sample_data/diamonds_prices.csv')
df = df.drop(["cut", "color", "clarity"], axis=1).drop(df.columns [0], axis=1)
print(df.head())
df.isnull().sum()

corr_matrix = df.corr()
print(corr_matrix)

corr_target = corr_matrix["price"].sort_values(ascending=False)
print(corr_target)

X = df["carat"].values
y = df["price"].values

def mserror(X, w1, w0, y):
    y_pred = w1 * X[:] + w0
    return np.sum((y - y_pred) ** 2) / len(y_pred)


def gr_mserror(X, w1, w0, y):
    y_pred = w1 * X[:] + w0
    return np.array([2 / len(X) * np.sum((y - y_pred)) * (-1),
                     2 / len(X) * np.sum((y - y_pred) * (-X[:]))])


eps = 0.0001
w1 = 0
w0 = 0

learning_rate = 0.1

next_w1 = w1
next_w0 = w0
n = 100000
for i in range(n):
    cur_w1 = next_w1
    cur_w0 = next_w0
    next_w0 = cur_w0 - learning_rate * gr_mserror(X, cur_w1, cur_w0, y)[0]
    next_w1 = cur_w1 - learning_rate * gr_mserror(X, cur_w1, cur_w0, y)[1]

    print(f"Итерации: {i}")
    print(f"Текущиая точка {cur_w1, cur_w0}| Следующая точка {next_w1, next_w0}")
    print(f"MSE {mserror(X, cur_w1, cur_w0, y)}")

    if (abs(cur_w1 - next_w1) <= eps) and (abs(cur_w0 - next_w0) <= eps):
        break

fig = plt.figure(figsize=(10,6))

x = np.arange(0, 5)

man_model = cur_w1 * x + cur_w0

plt.plot(x, man_model, linewidth=2, color = "r", label=f'вручную = {cur_w1:.2f}x + {cur_w0:.2f}')
plt.scatter(X, y, label='Исходные данные')
plt.grid()
plt.xlabel("Карат")
plt.ylabel("Цена")
plt.legend (prop={'size': 16})
plt.show()

"""# Задание 3"""

df = pd.read_csv('sample_data/insurance.csv')
unique_regions = df['region'].unique()
print("Уникальные регионы: ", unique_regions)

regions = df['region'].unique()

grouped_data = [df['bmi'][df['region'] == region] for region in regions]
f_statistic, p_value = scipy.stats.f_oneway(*grouped_data)
print("Результаты однофакторного ANOVA теста:")
print("F-статистика:", f_statistic)
print("p-значение:", p_value)

model = ols('bmi ~ region', data=df).fit()
anova_table = sm.stats.anova_lm(model)
print(anova_table)

regions = df['region'].unique()
results = []
for i in range(len(regions)):
    for j in range(i + 1, len(regions)):
        region1 = regions[i]
        region2 = regions[j]
        group1 = df['bmi'][df['region'] == region1]
        group2 = df['bmi'][df['region'] == region2]
        t_statistic, p_value = ttest_ind(group1, group2)
        results.append((region1, region2, t_statistic, p_value))

# Поправка Бонферрони
p_values = [result[3] for result in results]
corrected_p_values = multipletests(p_values, method='bonferroni')[1]
print("Значимые различия между регионами с поправкой Бонферрони:")
for i, result in enumerate(results):
    region1, region2, t_statistic, p_value = result
    corrected_p_value = corrected_p_values[i]
    print(
        f"{region1} vs {region2}: t-статистика = {t_statistic}, p-значение = {p_value}, Поправленное p-значение (Бонферрони) = {corrected_p_value}")

results = pairwise_tukeyhsd(df['bmi'], df['region'])
print(results)

results.plot_simultaneous()
plt.show()

model = ols('bmi ~ region + sex', data=df).fit()

print("Результаты двухфакторного ANOVA теста регион и пол:")
anova_table = anova_lm(model)
print(anova_table)

tukey_results = pairwise_tukeyhsd(df['bmi'], df['region'])
print(tukey_results)
tukey_results.plot_simultaneous()
plt.show()